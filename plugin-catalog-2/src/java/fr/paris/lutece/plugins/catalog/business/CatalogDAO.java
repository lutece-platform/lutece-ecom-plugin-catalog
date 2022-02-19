/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.catalog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Catalog objects
 */
public final class CatalogDAO implements ICatalogDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_catalog, name, description, price, vat FROM catalog_ WHERE id_catalog = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO catalog_ ( name, description, price, vat ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM catalog_ WHERE id_catalog = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE catalog_ SET id_catalog = ?, name = ?, description = ?, price = ?, vat = ? WHERE id_catalog = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_catalog, name, description, price, vat FROM catalog_";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_catalog FROM catalog_";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Catalog catalog, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , catalog.getName( ) );
            daoUtil.setString( nIndex++ , catalog.getDescription( ) );
            daoUtil.setInt( nIndex++ , catalog.getPrice( ) );
            daoUtil.setInt( nIndex++ , catalog.getVat( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                catalog.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Catalog load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        Catalog catalog = null;
	
	        if ( daoUtil.next( ) )
	        {
	            catalog = new Catalog();
	            int nIndex = 1;
	            
	            catalog.setId( daoUtil.getInt( nIndex++ ) );
	            catalog.setName( daoUtil.getString( nIndex++ ) );            
	            catalog.setDescription( daoUtil.getString( nIndex++ ) );            
	            catalog.setPrice( daoUtil.getInt( nIndex++ ) );            
	            catalog.setVat( daoUtil.getInt( nIndex ) );            
	        }
	
	        daoUtil.free( );
	        return catalog;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
	        daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Catalog catalog, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
	        daoUtil.setInt( nIndex++ , catalog.getId( ) );
	        daoUtil.setString( nIndex++ , catalog.getName( ) );
	        daoUtil.setString( nIndex++ , catalog.getDescription( ) );
	        daoUtil.setInt( nIndex++ , catalog.getPrice( ) );
	        daoUtil.setInt( nIndex++ , catalog.getVat( ) );
	        daoUtil.setInt( nIndex , catalog.getId( ) );
	
	        daoUtil.executeUpdate( );
	        daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Catalog> selectCatalogsList( Plugin plugin )
    {
        List<Catalog> catalogList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            Catalog catalog = new Catalog(  );
	            int nIndex = 1;
	            
	            catalog.setId( daoUtil.getInt( nIndex++ ) );
	            catalog.setName( daoUtil.getString( nIndex++ ) );
	            catalog.setDescription( daoUtil.getString( nIndex++ ) );
	            catalog.setPrice( daoUtil.getInt( nIndex++ ) );
	            catalog.setVat( daoUtil.getInt( nIndex ) );            
	
	            catalogList.add( catalog );
	        }
	
	        daoUtil.free( );
	        return catalogList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdCatalogsList( Plugin plugin )
    {
        List<Integer> catalogList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            catalogList.add( daoUtil.getInt( 1 ) );
	        }
	
	        daoUtil.free( );
	        return catalogList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectCatalogsReferenceList( Plugin plugin )
    {
        ReferenceList catalogList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            catalogList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        daoUtil.free( );
	        return catalogList;
    	}
    }
}
