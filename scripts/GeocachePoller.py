import logging
import urllib.request
import urllib.response
from urllib.error import URLError

logger = logging.getLogger("GeocachePoller")
logging.basicConfig(filename="C:\\GeocacheGroupingApp\\geocachepoller.log",
                    format='%(asctime)s %(message)s',
                    filemode='a+')
logger.setLevel(logging.DEBUG)

def getFile():
    logger.info("Start downloading latest cache list...")
    try:
        response = urllib.request.urlopen(
            "https://www.geocaching.hu/caches.geo?egylapon=25&dist_lat=&dist_lon=&filetype=gpx_groundspeak_uj&wp_logs_max=&waypoint_xml=%3Cfield+name%3D%22nev%22+deaccent%3D%22%C3%A1%C3%A9%C3%AD%C3%B3%C3%BA%C3%B6%C5%91%C3%BC%C5%B1%C3%81%C3%89%C3%8D%C3%93%C3%9A%C3%96%C5%90%C3%9C%C5%B0%22%2F%3E&description_xml=%3Cfield+name%3D%22kod%22+deaccent%3D%22%C3%A1%C3%A9%C3%AD%C3%B3%C3%BA%C3%B6%C5%91%C3%BC%C5%B1%C3%81%C3%89%C3%8D%C3%93%C3%9A%C3%96%C5%90%C3%9C%C5%B0%22%2F%3E%0D%0A+%3Cfield+name%3D%22leiras%22+deaccent%3D%22%C3%A1%C3%A9%C3%AD%C3%B3%C3%BA%C3%B6%C5%91%C3%BC%C5%B1%C3%81%C3%89%C3%8D%C3%93%C3%9A%C3%96%C5%90%C3%9C%C5%B0%22%2F%3E&content_type=default&content_disposition=default&filename=&compression=default&wp_limit=&profile_load=&profile_name=&submit_waypoints=Let%C3%B6lt%C3%A9s&any=&nickname=&fulldesc=&placer=&waypoint=&member=&nincs_meg_nekik=&dateid_min=&dateid_max=&diff_min=&diff_max=&terr_min=&terr_max=&length_min=&length_max=&alt_min=&alt_max=&db_m_min=&db_m_max=&db_j_min=&db_j_max=&db_n_min=&db_n_max=&db_o_min=&db_o_max=&db_l_min=&db_l_max=&rating_place_min=&rating_place_max=&rating_cache_min=&rating_cache_max=&rating_web_min=&rating_web_max=&rating_min=&rating_max=&dateposted_min=&dateposted_max=&elsolog_min=&elsolog_max=&utolsolog_min=&utolsolog_max=&db_f_min=&db_f_max=&dist_min=&dist_max=&login_user_id=34498&no_poi=i&id=google&filter=i")\
            .read()
    except URLError:
        logger.error("Error when downloading the file", )
        exit(1)
    logger.info("Download complete. File size: %d bytes", response.__sizeof__())
    try:
        f = open("C:\\GeocacheGroupingApp\\cache.xml", "wb")
    except OSError:
        logger.error("Error when creating file")
        exit(1)
    logger.info("File saving...")
    f.write(response)
    logger.info("File saved. Go to sleep...")


getFile()
