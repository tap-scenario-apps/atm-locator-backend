from flask import Flask, request, Response
import geocodioLocator as gcl
import logging
import os
import json

# Globals
_g_app = Flask(__name__)
_g_geocodioLocator : gcl.GeoLocator

@_g_app.route('/loc',methods = ['GET'])
def loc():

    if request.method == 'GET':

        address     = request.args.get('address')
        city        = request.args.get('city')
        state       = request.args.get('state')
        postalCode  = request.args.get('postalCode')


        try:

            loc = _g_geocodioLocator.translateToLocation(address, city, state, postalCode)

        except gcl.AuthError as ae:
            logging.error(str(ae))
            return Response(content_type='application/json', response="{error=" + str(ae) + "}", status=500)
        except gcl.DataError as de:
            logging.error(str(de))
            return Response(content_type='application/json', response="{error=" + str(de) + "}", status=400)
        except gcl.ServerError as se:
            logging.error(str(se))
            return Response(content_type='application/json', response="{error=" + str(se) + "}", status=500)
        except gcl.GeneralError as ge:
            logging.error(str(ge))
            return Response(content_type='application/json', response="{error=" + str(ge) + "}", status=500)


        res = {
            "latitude": loc.latitude,
            "longitude": loc.longitude
        }

        return Response(content_type='application/json', response=json.dumps(res), status=200)

    else:
        
        return Response(status=501)


def main():

    
    #print(__name__)
    logging.basicConfig(format='%(asctime)s,%(msecs)d %(levelname)-8s [%(filename)s:%(lineno)d]    %(message)s', datefmt='%Y-%m-%d:%H:%M:%S',level=logging.DEBUG)
    logging.info('Started')

    try:

        geocodio_api_key = os.environ['GEOCODIO_API_KEY']

    except KeyError:
        logging.error('Env variable GEOCODIO_API_KEY missing')
        return

    
    global _g_geocodioLocator
    _g_geocodioLocator = gcl.GeocodioLocator(geocodio_api_key)

    _g_app.run(host = '0.0.0.0', port=8080, debug=True)
    

    logging.info('Finished')



if __name__ == 'apiserver' or __name__ == '__main__':
    main()