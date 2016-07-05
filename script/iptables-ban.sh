#!/bin/bash
# Usage: iptables-ban.sh <dest port> <source ip>

TABLE_LINE="11"
REJECT_WITH="tcp-reset"

if [ -n "$2" ]
then
  sudo iptables -I INPUT $TABLE_LINE -s $2 -p tcp --dport $1 -j REJECT --reject-with $REJECT_WITH
fi
