// PathVisio WP Client
// Plugin that provides a WikiPathways client for PathVisio.
// Copyright 2013 developed for Google Summer of Code
//
// Licensed under the Apache License, Version 2.0 (the "License"); 
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
// 
// http://www.apache.org/licenses/LICENSE-2.0 
//  
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
// See the License for the specific language governing permissions and 
// limitations under the License.
//
package org.pathvisio.wpclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;

import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.xml.rpc.ServiceException;

import org.pathvisio.core.debug.Logger;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.wikipathways.client.WikiPathwaysClient;



import com.jgoodies.forms.layout.CellConstraints;

/**
 * This class creates the Table Model List containing the pathways  
 * 	@author Sravanthi Sinha
 * 	@version 1.0
 */
public class PathwayPanel extends JPanel 
{
	WikiPathwaysClientPlugin plugin;

	JTable resultTable;
	private JScrollPane resultspane;	
	
	public PathwayPanel(final WikiPathwaysClientPlugin plugin, WSSearchResult[] wsp, final File tmpDir) throws MalformedURLException, ServiceException 
	{
		this.plugin = plugin;

		setLayout(new BorderLayout());	

		JPanel searchOptBox = new JPanel();
		
		CellConstraints cc = new CellConstraints();	
	


		add(searchOptBox, BorderLayout.NORTH);

		// Center contains table model for results
		resultTable = new JTable();
		
		resultTable.setModel(new ResultTableModel(wsp,WikiPathwaysClientPlugin.loadClient().toString()));
		resultTable.setRowSorter(new TableRowSorter(resultTable.getModel()));
		resultspane = new JScrollPane(resultTable);

		add(resultspane, BorderLayout.CENTER);
		
	
		resultTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e) 
			{
				if (e.getClickCount() == 2) 
				{
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					ResultTableModel model = (ResultTableModel ) target.getModel();
					
					tmpDir.mkdirs();
					
					try
					{
						plugin.openPathwayWithProgress(WikiPathwaysClientPlugin.loadClient(),model.getValueAt(row, 0).toString(), 0, tmpDir);
					}
					catch (Exception ex) 
					{
						JOptionPane.showMessageDialog(PathwayPanel.this,ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
						Logger.log.error("Error", ex);
					}
				}
			}
		});
	}


	
	

}
