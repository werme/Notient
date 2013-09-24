import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        setupInitialData(app);
    }

    private void setupInitialData(Application app) {
        if(Ebean.find(Note.class).findRowCount() == 0) {
            Logger.info("Setting up initial data...");
            Ebean.save((List) Yaml.load("initial-data.yml"));
        }
    }
}