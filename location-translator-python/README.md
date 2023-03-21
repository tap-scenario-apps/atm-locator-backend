# Location-translator in python
---
This guide documents the steps required to run the python implementation of the [location-translator](../location-translator/README.md) service.


### Steps
---
1. Create a python virtual environment by running the following:

    ```shell
    $ python3 -m venv venv
    ```

2. Switch into the virtual environment by running the following: 
    ```shell
    $ source venv/bin/activate
    ```

3. Install the dependencies by running the following: 
    ```shell
    $ pip3 install -r requirements.txt
    ```

4.  Run the API server:
    
    First set the GEOCODIO_API_KEY env variable
    ```shell
    $ export GEOCODIO_API_KEY=<your api key>
    ```
    If you'd like to change the default listening port which is 8080 do the following:
    ```shell
    $ export GEOCODIO_PORT=<listening Port>
    ```
    then run:
    ```shell
    $ python3 src/apiserver.py
    ```

    At this point API server is running and listening on GEOCODIO_PORT or the default port 8080

### TAP Deployment

The TAP workload definition is contained inside the `/config/workload.yaml` file, and it defines values for the `GEOCODIO_API_KEY` and `GEOCODIO_PORT` environment variables.  A `Procfile` also exists in the root directory which defines what command will be used for the `web` process types.  The Procfile launches the app using the command 
`python3 src/apiserver.py`, however future versions may choose to use another web server frontend such as [Gunicorn](https://gunicorn.org/).

The workload requires at minimum the `base` ClusterBuilder as the `tiny` ClusterBuilder does not fully support the needed dependencies to build this python application.  

To build the application on TAP, simply run the following command from the `config` directory.

```shell
kubectl apply -f workload.yaml
```