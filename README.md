#### Instructions to run lambda function in local

```sh

./gradle build

docker run --rm -v "$PWD/build/docker":/var/task lambci/lambda:java8 fw.DiffHandler '{"source": "source", "destination": "destination"}'
```