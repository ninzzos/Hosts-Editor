/*
 * San Services HN, 2016
 *
 * All rights reserved.
 * Copying of this software or parts of this software is a violation
 * of U.S. and international laws and will be prosecuted.
 *
 * Author(s): Jose Munoz ( Software Engineer )
 *
 * Created on Jan 03, 2017 12:00:00 AM
 *
 * 06/06/2016 - Jose Munoz
 *      Initial Implementation
 */

package co.superiortech.hostseditor.model;

import java.io.Serializable;

/**
 * HostsRecord
 *
 * @author Jose Munoz
 * @version 1.0.0
 */
public class HostsRecord implements Serializable{
    
    private static final long serialVersionUID = 3283952040014971188L;
    
    private String ipAddress;
    private String hostname;

    public HostsRecord(String ipAddress, String hostname) {
        this.ipAddress = ipAddress;
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
