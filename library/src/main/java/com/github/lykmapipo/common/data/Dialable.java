package com.github.lykmapipo.common.data;

/**
 * A contract to be implemented by object that can be dialed or called.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Dialable {
    /**
     * Valid phone number for the {@link Dialable}
     *
     * @return phone number
     * @since 0.1.0
     */
    String getPhoneNumber();
}
