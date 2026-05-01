#!/usr/bin/env python3
"""
Convert documentation-generator files to provenance format.

Changes made:
- documentation.yml / documentation-*.yml  -> provenance.yml / provenance-*.yml
- generator-configuration.yml              -> provenance-configuration.yml
- directory documentation-generator/       -> provenance/
- $schema URL https://patrickfust.github.io/documentation-generator/vX/documentation-schema.json
           -> https://patrickfust.github.io/provenance/v1/provenance-schema.json
- documentationTitle                       -> provenanceTitle
- dk.fust.docgen.erdiagram.*              -> dk.fust.provenance.generator.erdiagram.*
- dk.fust.docgen.sqlscript.*              -> dk.fust.provenance.generator.sqlscript.*
- dk.fust.docgen.destination.*            -> dk.fust.provenance.destination.*
- dk.fust.(docgen|provenance).csv.*       -> dk.fust.provenance.destination.csv.*
- dk.fust.(docgen|provenance).excel.*     -> dk.fust.provenance.destination.excel.*
- ConfluenceDestination                   -> dk.fust.provenance.destination.confluence.destination.ConfluenceDestination
- References to documentation-*.yml files in YAML values are updated
  to provenance-*.yml
- documentationFile key                   -> provenanceFile
- pom files                               -> Replace Documentation Generator plugin with Provenance
"""

import os
import re
import shutil
import argparse
from pathlib import Path

# ---------------------------------------------------------------------------
# Transformation helpers
# ---------------------------------------------------------------------------

def transform_content(text: str) -> str:
    """Apply all in-file text transformations."""

    return transform_yaml_content(text)


def transform_yaml_content(text: str) -> str:
    """Apply YAML-specific transformations."""

    # 1. $schema: replace documentation-generator schema URL with provenance schema URL
    text = re.sub(
        r'https://patrickfust\.github\.io/documentation-generator/v\d+/documentation-schema\.json',
        'https://patrickfust.github.io/provenance/v1/provenance-schema.json',
        text,
    )

    # 2. documentationTitle -> provenanceTitle
    text = re.sub(r'\bdocumentationTitle\b', 'provenanceTitle', text)

    # 3. Class package remapping.
    #    Generator classes move under provenance.generator.*
    text = re.sub(
        r'\bdk\.fust\.docgen\.(erdiagram|sqlscript|datadict|datalineage)\.',
        r'dk.fust.provenance.generator.\1.',
        text,
    )

    #    Destination classes stay under provenance.destination.*
    text = re.sub(
        r'\bdk\.fust\.docgen\.destination\.',
        'dk.fust.provenance.destination.',
        text,
    )

    #    ConfluenceDestination has a dedicated destination package path.
    text = re.sub(
        r'\bdk\.fust\.(?:docgen|provenance)\.(?:destination(?:\.confluence(?:\.destination)?)?|confluence\.destination)\.ConfluenceDestination\b',
        'dk.fust.provenance.destination.confluence.destination.ConfluenceDestination',
        text,
    )

    #    CSV/Excel table formatter classes belong under provenance.destination.*
    text = re.sub(
        r'\bdk\.fust\.(?:docgen|provenance)\.(csv|excel)\.format\.table\.',
        r'dk.fust.provenance.destination.\1.format.table.',
        text,
    )

    #    Fallback for any remaining docgen package references.
    text = re.sub(r'\bdk\.fust\.docgen\b', 'dk.fust.provenance', text)

    # 4. References to documentation files inside YAML values:
    #    *File: documentation-foo.yml  -> provenance-foo.yml
    #    *File: some/path/documentation-foo.yml -> some/path/provenance-foo.yml
    #    Also handle bare "documentation.yml"
    text = re.sub(
        r'((?:documentationFile|provenanceFile)\s*:\s*[^\n]*?)documentation(-[^\s/]+)?\.yml',
        lambda m: m.group(0).replace(
            'documentation' + (m.group(2) or '') + '.yml',
            'provenance' + (m.group(2) or '') + '.yml',
        ),
        text,
    )

    # 5. documentation-generator/ path segment in config references -> provenance/
    text = re.sub(
        r'((?:documentationFile|provenanceFile)\s*:\s*[^\n]*?)documentation-generator/',
        r'\1provenance/',
        text,
    )

    # 6. documentationFile key -> provenanceFile
    text = re.sub(r'\bdocumentationFile\b', 'provenanceFile', text)

    # 7. provenance-configuration.yml reference inside files (edge case)
    text = re.sub(
        r'\bgenerator-configuration\.yml\b',
        'provenance-configuration.yml',
        text,
    )

    return text


def _provenance_plugin_block(indent: str) -> str:
    """Build the target provenance Maven plugin block with matching indentation."""
    lines = [
        f"{indent}<plugin>",
        f"{indent}    <groupId>dk.fust.provenance</groupId>",
        f"{indent}    <artifactId>provenance-maven-plugin</artifactId>",
        f"{indent}    <version>${{provenanceGenerator.version}}</version>",
        f"{indent}    <dependencies>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-generator-erdiagram</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-generator-sql-script</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-generator-data-dictionary</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-generator-datalineage</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-destination-confluence</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-destination-csv</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}        <dependency>",
        f"{indent}            <groupId>dk.fust.provenance</groupId>",
        f"{indent}            <artifactId>provenance-destination-excel</artifactId>",
        f"{indent}            <version>${{provenanceGenerator.version}}</version>",
        f"{indent}        </dependency>",
        f"{indent}    </dependencies>",
        f"{indent}    <configuration>",
        f"{indent}        <provenanceConfigurationFile>provenance-configuration.yml</provenanceConfigurationFile>",
        f"{indent}    </configuration>",
        f"{indent}    <executions>",
        f"{indent}        <execution>",
        f"{indent}            <goals>",
        f"{indent}                <goal>generate</goal>",
        f"{indent}            </goals>",
        f"{indent}        </execution>",
        f"{indent}    </executions>",
        f"{indent}</plugin>",
    ]
    return "\n".join(lines)


def transform_pom_content(text: str) -> str:
    """Apply pom.xml-specific transformations."""

    plugin_pattern = re.compile(r'(?P<indent>^[ \t]*)<plugin>\s*.*?^[ \t]*</plugin>', re.M | re.S)

    def replace_plugin(match: re.Match) -> str:
        plugin_block = match.group(0)
        if (
            '<groupId>dk.fust.docgen</groupId>' in plugin_block
            and '<artifactId>documentation-generator-maven-plugin</artifactId>' in plugin_block
        ):
            return _provenance_plugin_block(match.group('indent'))
        return plugin_block

    text = plugin_pattern.sub(replace_plugin, text)

    # Normalize remaining legacy plugin snippets if already partially migrated.
    text = re.sub(r'<groupId>dk\.fust\.docgen</groupId>', '<groupId>dk.fust.provenance</groupId>', text)
    text = re.sub(
        r'<artifactId>documentation-generator-maven-plugin</artifactId>',
        '<artifactId>provenance-maven-plugin</artifactId>',
        text,
    )
    text = re.sub(r'\$\{documentationGenerator\.version\}', '${provenanceGenerator.version}', text)
    text = re.sub(
        r'<documentationConfigurationFile>\s*generator-configuration\.yml\s*</documentationConfigurationFile>',
        '<provenanceConfigurationFile>provenance-configuration.yml</provenanceConfigurationFile>',
        text,
    )
    text = re.sub(r'<goal>generateDocumentation</goal>', '<goal>generate</goal>', text)
    text = re.sub(r'\bgenerator-configuration\.yml\b', 'provenance-configuration.yml', text)

    return text


def new_filename(name: str) -> str:
    """Return the renamed filename (or the same name if no change needed)."""
    if name == 'generator-configuration.yml':
        return 'provenance-configuration.yml'
    if name == 'documentation.yml':
        return 'provenance.yml'
    if name.startswith('documentation-') and name.endswith('.yml'):
        return 'provenance-' + name[len('documentation-'):]
    return name


# ---------------------------------------------------------------------------
# Directory walking
# ---------------------------------------------------------------------------

def convert_directory(root: str, dry_run: bool = False, backup: bool = False):
    root_path = Path(root).resolve()

    # Collect files to process so we don't trip over renames mid-walk
    targets = []
    directory_targets = []
    for dirpath, dirnames, filenames in os.walk(root_path):
        # Skip hidden dirs and common build/cache dirs
        dirnames[:] = [
            d for d in dirnames
            if not d.startswith('.')
            and d not in ('target', '__pycache__', 'node_modules', '.git')
        ]

        for dirname in dirnames:
            if dirname == 'documentation-generator':
                directory_targets.append(Path(dirpath) / dirname)

        for filename in filenames:
            is_yaml = filename.endswith('.yml')
            is_pom_xml = filename.startswith('pom') and filename.endswith('.xml')
            if is_yaml or is_pom_xml:
                targets.append(Path(dirpath) / filename)

    renamed_files = 0
    renamed_dirs = 0
    modified = 0

    for filepath in targets:
        original_name = filepath.name
        new_name = new_filename(original_name)

        # Read & transform content
        try:
            original_text = filepath.read_text(encoding='utf-8')
        except Exception as e:
            print(f'  [SKIP] Cannot read {filepath}: {e}')
            continue

        if filepath.suffix == '.yml':
            new_text = transform_yaml_content(original_text)
        else:
            new_text = transform_pom_content(original_text)

        content_changed = new_text != original_text
        name_changed = new_name != original_name

        if not content_changed and not name_changed:
            continue

        new_path = filepath.parent / new_name

        if dry_run:
            if content_changed:
                print(f'  [DRY] Would modify content: {filepath}')
            if name_changed:
                print(f'  [DRY] Would rename: {filepath} -> {new_path}')
            continue

        if backup and (content_changed or name_changed):
            bak = filepath.with_suffix(filepath.suffix + '.bak')
            shutil.copy2(filepath, bak)

        if content_changed:
            filepath.write_text(new_text, encoding='utf-8')
            print(f'  [MOD]  {filepath}')
            modified += 1

        if name_changed:
            filepath.rename(new_path)
            print(f'  [REN]  {filepath.name} -> {new_name}  (in {filepath.parent})')
            renamed_files += 1

    # Rename nested documentation-generator directories first.
    directory_targets.sort(key=lambda p: len(p.parts), reverse=True)

    for directory_path in directory_targets:
        new_directory_path = directory_path.parent / 'provenance'

        if dry_run:
            print(f'  [DRY] Would rename directory: {directory_path} -> {new_directory_path}')
            continue

        if not directory_path.exists():
            continue

        if new_directory_path.exists():
            print(f'  [SKIP] Cannot rename {directory_path}: destination already exists')
            continue

        directory_path.rename(new_directory_path)
        print(f'  [REN]  dir {directory_path.name} -> provenance  (in {directory_path.parent})')
        renamed_dirs += 1

    if not dry_run:
        print(
            f'\nDone. {modified} file(s) modified, '
            f'{renamed_files} file(s) renamed, {renamed_dirs} directorie(s) renamed.'
        )
    else:
        print('\nDry-run complete. No files were changed.')


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(
        description='Convert documentation-generator project files to provenance format.'
    )
    parser.add_argument(
        'directory',
        nargs='?',
        default=None,
        help='Root directory to process',
    )
    parser.add_argument(
        '--dry-run',
        action='store_true',
        help='Show what would be changed without making any changes',
    )
    parser.add_argument(
        '--backup',
        action='store_true',
        help='Create .bak copies of files before modifying/renaming',
    )
    args = parser.parse_args()

    if args.directory is None:
        parser.print_help()
        return

    print(f'Processing directory: {Path(args.directory).resolve()}')
    if args.dry_run:
        print('** DRY RUN - no changes will be made **\n')

    convert_directory(args.directory, dry_run=args.dry_run, backup=args.backup)


if __name__ == '__main__':
    main()
