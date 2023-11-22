import configparser
import logging
import sys
import urllib.request
import urllib.response
from urllib.error import URLError


# call usage: GeocachePoller.py <CONFIG_FILE>

def get_file():
    logger.info("Start downloading latest cache list...")
    try:
        response = (urllib.request.urlopen(
            config.get('GeocachePoller', 'geocache.download.url'))
                    .read())
    except URLError as inst:
        logger.error("Error when downloading the file")
        logger.error(inst.args)
        logger.error(inst)
        exit(1)
    logger.info("Download complete. File size: %d bytes", response.__sizeof__())
    try:
        f = open(config.get('GeocachePoller', 'geocache.file.path'), "wb")
    except OSError as inst:
        logger.error("Error when creating file")
        logger.error(inst.args)
        logger.error(inst)
        exit(1)
    logger.info("File saving...")
    f.write(response)
    logger.info("File saved. Go to sleep...")


config = configparser.RawConfigParser()
config.read(sys.argv[1])

logger = logging.getLogger("GeocachePoller")
logging.basicConfig(filename=config.get('GeocachePoller', 'geocache.poller.log.location'),
                    format='%(asctime)s %(message)s',
                    filemode='a+')
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler(sys.stdout))

get_file()
