#!/bin/bash
#
# Builds acme-stubs and deploys all artifacts into a local file-based Maven
# repository (local-maven-repo/) that can be bundled with S2I builds.
#

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_DIR="$SCRIPT_DIR/local-maven-repo"

echo "=============================================="
echo " Creating local Maven repository"
echo "=============================================="
echo ""
echo " Target: $REPO_DIR"
echo ""

# Clean previous repo
rm -rf "$REPO_DIR"
mkdir -p "$REPO_DIR"

# Step 1: Install stubs to local .m2 (needed for inter-module resolution)
echo ">>> Building and installing acme-stubs..."
mvn clean install -q -f "$SCRIPT_DIR/acme-stubs/pom.xml"
echo "    Done."
echo ""

# Step 2: Deploy stubs to file-based repo
echo ">>> Deploying acme-stubs to local-maven-repo/..."
mvn deploy -q -f "$SCRIPT_DIR/acme-stubs/pom.xml" \
  -DaltDeploymentRepository="local-repo::default::file://$REPO_DIR" \
  -DskipTests
echo "    Done."
echo ""

# Step 3: Deploy the acme-common parent POM separately (it's a standalone module)
echo ">>> Deploying acme-common parent POM..."
mvn deploy -q -f "$SCRIPT_DIR/acme-stubs/acme-common/pom.xml" \
  -DaltDeploymentRepository="local-repo::default::file://$REPO_DIR" \
  -DskipTests
echo "    Done."
echo ""

echo "=============================================="
echo " Local Maven repository created!"
echo "=============================================="
echo ""
echo " Location: $REPO_DIR"
echo " Artifacts: $(find "$REPO_DIR" -name '*.jar' -o -name '*.pom' | wc -l | tr -d ' ') files"
echo ""
echo " To use in an S2I build, commit local-maven-repo/ along with"
echo " configuration/settings.xml and .s2i/environment."
echo ""
