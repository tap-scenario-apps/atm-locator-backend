# Location-translator in Node.js
---
This guide documents the steps required to run the Node.js implementation of the [location-translator](../location-translator/README.md) service.


### Steps
---

1. Modify the `.envrc` file to include your custom values for the `GEOCODIO_PORT` and `GEOCODIO_API_KEY` environment variables.

2.  Run the API server:

    ```shell
    $ node src/apiserver.py
    ```

    At this point API server is running and listening on `GEOCODIO_PORT` or the default port `8080`

### TAP Deployment

The TAP workload definition is contained inside the `/config/workload.yaml` file.

The workload requires at minimum the `base` ClusterBuilder as the `tiny` ClusterBuilder does not fully support the needed dependencies to build this Node.js application.

To build the application on TAP, simply run the following command from the `config` directory.

```shell
kubectl apply -f workload.yaml
```