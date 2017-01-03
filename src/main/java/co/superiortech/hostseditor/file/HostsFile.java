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
package co.superiortech.hostseditor.file;

import co.superiortech.hostseditor.model.HostsRecord;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * HostsFile
 *
 * @author Jose Munoz
 * @version 1.0.0
 */
public class HostsFile {

    private final String location = SystemUtils.IS_OS_WINDOWS ? "C:\\windows\\system32\\drivers\\etc\\hosts" : "/etc/hosts";
    private final File hostsFile;

    public HostsFile() {
        this.hostsFile = new File(this.location);
    }

    public ArrayList<HostsRecord> getHostRecords() throws IOException {
        ArrayList<HostsRecord> records = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(this.hostsFile));
        String chunk = null;
        String[] pieces = null;

        while ((chunk = br.readLine()) != null) {
            if (!(chunk.startsWith("#") || chunk.startsWith(":"))) {
                pieces = chunk.split("\t");
                if (pieces.length == 2) {
                    records.add(new HostsRecord(pieces[0], pieces[1]));
                }
            }
        }

        return records;
    }

    public void saveHostRecords(List<HostsRecord> records, boolean keepBackup) throws FileNotFoundException, IOException {
        String tmp = null;
        try (FileReader reader = new FileReader(this.hostsFile);
                BufferedReader br = new BufferedReader(reader);
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.hostsFile));) {

            if (keepBackup) {
                try (FileWriter copy = new FileWriter(new File(this.location + ".bak"));) {
                    IOUtils.copy(reader, copy);
                    reader.reset();
                }
            }

            ArrayList<HostsRecord> currentRecords = new ArrayList<>();

            while ((tmp = br.readLine()) != null) {
                if (tmp.startsWith("#") || tmp.startsWith(":")) {
                    bw.append(tmp);
                    bw.append("\n");
                } else {
                    String[] pieces = tmp.split("\t");
                    if (pieces.length == 2) {
                        currentRecords.add(new HostsRecord(pieces[0], pieces[1]));
                    }
                }
            }
            
            if (!currentRecords.containsAll(records)) {
                for (HostsRecord record : records) {
                    tmp = record.getIpAddress() + "\t" + record.getHostname();
                    bw.append(tmp);
                    bw.append("\n");
                }
            }
        }
    }
}
