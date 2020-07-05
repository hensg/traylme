mysqldump --single-transaction --flush-logs --master-data \
           --all-databases > backup_`date '+%Y_%m_%d_%H%M%S'`.sql
