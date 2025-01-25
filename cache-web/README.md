### Arch

![](/docs/docker-compose.png)

### Build & Run

cache_web docker build and run separately,
```shell
sudo docker build \
-t suriyaprakhash/cache-web:0.0.1 \
--build-arg SRC_CODE_PATH=home\suriya\projects\cache-web \
--no-cache --progress=plain .
```

cache_web docker build and run as docker-compose.yml,
```shell
docker-compose up -d

# to force cache_web rebuild for any code change
docker-compose up -d --no-deps --build cache_web
```