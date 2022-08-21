package com.jakz.hm.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jakz.hm.Text;
import com.jakz.hm.TextReference;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.FilterableDataSource;
import com.pixbits.lib.ui.table.TableModel;

public class StringsTable extends JPanel
{
  private FilterableDataSource<Text> data;

  private TableModel<Text> model;
  private JTable table;
  
  public StringsTable()
  {
    table = new JTable();
    table.setAutoCreateRowSorter(true);
    model = new Model(table);
    
    model.addColumn(new ColumnSpec<Text, TextReference>("", TextReference.class, t -> t.reference));
    model.addColumn(new ColumnSpec<Text, String>("", String.class, t -> t.text));
    
    setLayout(new BorderLayout());
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  
  public void refresh(Collection<Text> data)
  {
    this.data = FilterableDataSource.of(data);
    model.setData(this.data);
    model.fireTableDataChanged();
  }
  
  private class Model extends TableModel<Text>
  {
    Model(JTable table)
    {
      super(table, DataSource.empty());
    }
  }
}
