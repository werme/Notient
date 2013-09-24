import com.avaje.ebean.Ebean;
import models.Note;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import java.util.*;

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