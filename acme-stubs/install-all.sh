#!/bin/bash
#
# Installs all ACME stub dependencies to the local Maven repository (~/.m2/repository).
# Run this before building the mta-test-app project in beforeMTAMigration/.
#
# Usage: cd acme-stubs && ./install-all.sh
#

set -e

echo "=============================================="
echo " Installing ACME Stub Dependencies"
echo "=============================================="
echo ""

# Install the parent POM first (standalone, not part of reactor)
echo ">>> Installing acme-common parent POM..."
cd acme-common
mvn install -q
cd ..
echo "    Done."
echo ""

# Build and install all modules via the reactor (respects dependency order)
echo ">>> Building and installing all stub modules via reactor..."
mvn clean install -q
echo ""

echo "=============================================="
echo " All ACME stub dependencies installed!"
echo "=============================================="
echo ""
echo " You can now build the mta-test-app project:"
echo "   cd ../beforeMTAMigration"
echo "   mvn clean package"
echo ""
