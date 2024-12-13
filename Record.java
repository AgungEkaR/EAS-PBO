import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Base class for common attributes
abstract class Record {
    protected String description;
    protected Date date;

    public Record(String description) {
        this.description = description;
        this.date = new Date();
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
