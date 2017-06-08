#!/usr/bin/env bash

echo "Dropping schema"
echo "mysql -u root -h 127.0.0.1 -e 'DROP SCHEMA lvote'"
mysql -u root -h 127.0.0.1 -e 'DROP SCHEMA lvote'

echo "Creating schema"
echo "mysql -u root -h 127.0.0.1 -e 'CREATE SCHEMA lvote'"
mysql -u root -h 127.0.0.1 -e 'CREATE SCHEMA lvote'
