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
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.catalog.web;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.util.List;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.plugins.catalog.business.Catalog;
import fr.paris.lutece.plugins.catalog.business.CatalogHome;
/**
 * This is the business class test for the object Catalog
 */
public class CatalogXPageTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String DESCRIPTION1 = "Description1";
    private static final String DESCRIPTION2 = "Description2";
    private static final int PRICE1 = 1;
    private static final int PRICE2 = 2;
    private static final int VAT1 = 1;
    private static final int VAT2 = 2;

public void testXPage(  ) throws AccessDeniedException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		CatalogXPage xpage = new CatalogXPage( );
		assertNotNull( xpage.getCreateCatalog( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createCatalog" ));
		
		LocalVariables.setLocal(config, request, response);
		
		request.addParameter( "action" , "createCatalog" );
        request.addParameter( "name" , NAME1 );
        request.addParameter( "description" , DESCRIPTION1 );
        request.addParameter( "price" , String.valueOf( PRICE1) );
        request.addParameter( "vat" , String.valueOf( VAT1) );
		request.setMethod( "POST" );
        
        assertNotNull( xpage.doCreateCatalog( request ) );

		//modify Catalog	
		List<Integer> listIds = CatalogHome.getIdCatalogsList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyCatalog( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
        request.addParameter( "name" , NAME2 );
        request.addParameter( "description" , DESCRIPTION2 );
        request.addParameter( "price" , String.valueOf( PRICE2) );
        request.addParameter( "vat" , String.valueOf( VAT2) );

		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyCatalog" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doModifyCatalog( request ) );

		//do confirm remove Catalog
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveCatalog" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveCatalog" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveCatalog( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove Catalog
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeCatalog" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeCatalog" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveCatalog( request ) );

    }
    
}
