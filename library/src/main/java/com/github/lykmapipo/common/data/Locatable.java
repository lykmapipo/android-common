package com.github.lykmapipo.common.data;

/**
 * A contract to be implemented by object that will be presented on map.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Locatable {
    /**
     * Location address
     *
     * @return applicable address
     * @since 0.1.0
     */
    String getAddress();

    /**
     * Location latitude
     *
     * @return valid latitude
     * @since 0.1.0
     */
    Float getLatitude();

    /**
     * Location longitude
     *
     * @return valid longitude
     * @since 0.1.0
     */
    Float getLongitude();
}
