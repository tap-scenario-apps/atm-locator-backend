# Location-translator in python
---
This guide documents the steps required to run the python implementation of the location translator 


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
    then run:
    ```shell
    $ python3 src/apiserver.py
    ```

    At this point API server is running and listening on port 8080

