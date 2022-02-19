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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.catalog.business;

import fr.paris.lutece.test.LuteceTestCase;


/**
 * This is the business class test for the object Catalog
 */
public class CatalogBusinessTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String DESCRIPTION1 = "Description1";
    private static final String DESCRIPTION2 = "Description2";
    private static final int PRICE1 = 1;
    private static final int PRICE2 = 2;
    private static final int VAT1 = 1;
    private static final int VAT2 = 2;

	/**
	* test Catalog
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        Catalog catalog = new Catalog();
        catalog.setName( NAME1 );
        catalog.setDescription( DESCRIPTION1 );
        catalog.setPrice( PRICE1 );
        catalog.setVat( VAT1 );

        // Create test
        CatalogHome.create( catalog );
        Catalog catalogStored = CatalogHome.findByPrimaryKey( catalog.getId( ) );
        assertEquals( catalogStored.getName() , catalog.getName( ) );
        assertEquals( catalogStored.getDescription() , catalog.getDescription( ) );
        assertEquals( catalogStored.getPrice() , catalog.getPrice( ) );
        assertEquals( catalogStored.getVat() , catalog.getVat( ) );

        // Update test
        catalog.setName( NAME2 );
        catalog.setDescription( DESCRIPTION2 );
        catalog.setPrice( PRICE2 );
        catalog.setVat( VAT2 );
        CatalogHome.update( catalog );
        catalogStored = CatalogHome.findByPrimaryKey( catalog.getId( ) );
        assertEquals( catalogStored.getName() , catalog.getName( ) );
        assertEquals( catalogStored.getDescription() , catalog.getDescription( ) );
        assertEquals( catalogStored.getPrice() , catalog.getPrice( ) );
        assertEquals( catalogStored.getVat() , catalog.getVat( ) );

        // List test
        CatalogHome.getCatalogsList( );

        // Delete test
        CatalogHome.remove( catalog.getId( ) );
        catalogStored = CatalogHome.findByPrimaryKey( catalog.getId( ) );
        assertNull( catalogStored );
        
    }
    
    
     

}