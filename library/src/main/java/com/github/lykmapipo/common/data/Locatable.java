package com.github.lykmapipo.common.data;

/**
 * A contract to be implemented by object that will be presented on map.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Locatable {
    String getAddress();

    Float getLatitude();

    Float getLongitude();
}
