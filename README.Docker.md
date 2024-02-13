### Inital configs
Copy sample environment variable and password files and replace values if necesary
```bash
# Copy environment variables file
# NOTE!!! Change variable CONTAINER_UID with your machine user uid (id -u)
cp .env.sample .env

# Copy dbs password files
cp ./docker/mariadb/secrets/password.sample.txt ./docker/mariadb/secrets/password.txt
cp ./docker/mariadb/secrets/root-password.sample.txt ./docker/mariadb/secrets/root-password.txt
```

### Building and running your application

When you're ready, start your application by running:
`docker compose up --build`.

Your application will be available at http://localhost:8080. if you don't change the application port

### Deploying your application to the cloud

First, build your image, e.g.: `docker build -t myapp .`.
If your cloud uses a different CPU architecture than your development
machine (e.g., you are on a Mac M1 and your cloud provider is amd64),
you'll want to build the image for that platform, e.g.:
`docker build --platform=linux/amd64 -t myapp .`.

Then, push it to your registry, e.g. `docker push myregistry.com/myapp`.

Consult Docker's [getting started](https://docs.docker.com/go/get-started-sharing/)
docs for more detail on building and pushing.