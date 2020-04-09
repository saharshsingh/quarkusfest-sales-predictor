# quarkusfest-sales-predictor

## Database

The project uses the database found at https://relational.fit.cvut.cz/dataset/Walmart. Following are instructions from that web page on how to retrieve this data.

1. Open your favourite MariaDB client (MySQL Workbench works, but see FAQ)
1. Use following credentials:
    - hostname: `relational.fit.cvut.cz`
    - port: `3306`
    - username: `guest`
    - password: `relational`
1. Export `Walmart` database (or other version of the dataset, if available) in your favourite format (e.g. CSV or SQL dump).

### Creating and importing a MySQL dump

1. Create a persistent MySQL instance in OCP

        oc new-app --name=walmart-data mysql-persistent \
            -p DATABASE_SERVICE_NAME=walmart-data \
            -p MYSQL_ROOT_PASSWORD=password \
            -p MYSQL_USER=walmart \
            -p MYSQL_PASSWORD=password \
            -p MYSQL_DATABASE=Walmart \
            -p VOLUME_CAPACITY=4Gi \
            -p MEMORY_LIMIT=4Gi

1. Create dump using MySQL 5.7 compliant `mysqldump` tool.

        mysqldump -uguest -prelational --protocol=tcp --host=relational.fit.cvut.cz Walmart > /tmp/dump.sql

1. You might get a warning like `mysqldump: [Warning] Using a password on the command line interface can be insecure.` in the top line of the `/tmp/dump.sql` file. If so, remove it. 

1. Import dump using `mysql`

        oc rsh walmart-data-[pod-id-suffix] bash -c \
            "mysql -uwalmart -ppassword Walmart" < /tmp/dump.sql

## API

Once the database is up and running, use `oc apply` to create the API resources.

        oc apply -f sales-forecast-api/deployment

Start build to trigger first deployment

        oc start-build forecast-api --follow

### Endpoints

1. `GET /api/stores`
2. `GET /api/forecasts?date=2020-01-01&storeID=1`
3. `GET /health`
4. `GET /metrics`

