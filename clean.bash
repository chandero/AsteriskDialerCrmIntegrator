/opt/crmintegrator/bin/stop.sh
sqlite3 /opt/crmintegrator/db/CrmAsterisk.db "DELETE FROM crm_list_info;"
rm -f /opt/crmintegrator/logs/*
/opt/crmintegrator/bin/start.sh
