package models;

import com.avaje.ebean.Ebean;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class TagsTest extends WithApplication {

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
  }

  @Test
  public void createAndRetrievetag() {
    new Tag("My tag").save();
    Tag mytag = Tag.find.where().eq("title", "My tag").findUnique();
    assertNotNull(mytag);
    assertEquals("My tag", mytag.title);
  }

  @Test
  public void deletetag() {
    Tag mytag = Tag.find.where().eq("title", "Test tag title").findUnique();
    Tag.delete(mytag.id);

    Tag myDeletedtag = Tag.find.where().eq("title", "Test tag title").findUnique();
    assertNull(myDeletedtag);
  }
}