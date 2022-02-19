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

package fr.paris.lutece.plugins.catalog.web;

import fr.paris.lutece.plugins.catalog.business.Catalog;
import fr.paris.lutece.plugins.catalog.business.CatalogHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;

import java.util.Map;
import javax.servlet.http.HttpServletRequest; 

/**
 * This class provides the user interface to manage Catalog xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "catalog" , pageTitleI18nKey = "catalog.xpage.catalog.pageTitle" , pagePathI18nKey = "catalog.xpage.catalog.pagePathLabel" )
public class CatalogXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_CATALOGS = "/skin/plugins/catalog/manage_catalogs.html";
    private static final String TEMPLATE_CREATE_CATALOG = "/skin/plugins/catalog/create_catalog.html";
    private static final String TEMPLATE_MODIFY_CATALOG = "/skin/plugins/catalog/modify_catalog.html";
    
    // Parameters
    private static final String PARAMETER_ID_CATALOG = "id";
    
    // Markers
    private static final String MARK_CATALOG_LIST = "catalog_list";
    private static final String MARK_CATALOG = "catalog";
    
    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_CATALOG = "catalog.message.confirmRemoveCatalog";
    
    // Views
    private static final String VIEW_MANAGE_CATALOGS = "manageCatalogs";
    private static final String VIEW_CREATE_CATALOG = "createCatalog";
    private static final String VIEW_MODIFY_CATALOG = "modifyCatalog";

    // Actions
    private static final String ACTION_CREATE_CATALOG = "createCatalog";
    private static final String ACTION_MODIFY_CATALOG = "modifyCatalog";
    private static final String ACTION_REMOVE_CATALOG = "removeCatalog";
    private static final String ACTION_CONFIRM_REMOVE_CATALOG = "confirmRemoveCatalog";

    // Infos
    private static final String INFO_CATALOG_CREATED = "catalog.info.catalog.created";
    private static final String INFO_CATALOG_UPDATED = "catalog.info.catalog.updated";
    private static final String INFO_CATALOG_REMOVED = "catalog.info.catalog.removed";
    
    // Session variable to store working values
    private Catalog _catalog;
    
    /**
     * return the form to manage catalogs
     * @param request The Http request
     * @return the html code of the list of catalogs
     */
    @View( value = VIEW_MANAGE_CATALOGS, defaultView = true )
    public XPage getManageCatalogs( HttpServletRequest request )
    {
        _catalog = null;
        Map<String, Object> model = getModel(  );
        model.put( MARK_CATALOG_LIST, CatalogHome.getCatalogsList(  ) );
        
        return getXPage( TEMPLATE_MANAGE_CATALOGS, getLocale( request ), model );
    }

    /**
     * Returns the form to create a catalog
     *
     * @param request The Http request
     * @return the html code of the catalog form
     */
    @View( VIEW_CREATE_CATALOG )
    public XPage getCreateCatalog( HttpServletRequest request )
    {
        _catalog = ( _catalog != null ) ? _catalog : new Catalog(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATALOG, _catalog );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_CATALOG ) );

        return getXPage( TEMPLATE_CREATE_CATALOG, getLocale( request ), model );
    }

    /**
     * Process the data capture form of a new catalog
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_CATALOG )
    public XPage doCreateCatalog( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _catalog, request, getLocale( request ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_CATALOG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _catalog ) )
        {
            return redirectView( request, VIEW_CREATE_CATALOG );
        }

        CatalogHome.create( _catalog );
        addInfo( INFO_CATALOG_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }

    /**
     * Manages the removal form of a catalog whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException {@link fr.paris.lutece.portal.service.message.SiteMessageException}
     */
    @Action( ACTION_CONFIRM_REMOVE_CATALOG )
    public XPage getConfirmRemoveCatalog( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );

        UrlItem url = new UrlItem( getActionFullUrl( ACTION_REMOVE_CATALOG ) );
        url.addParameter( PARAMETER_ID_CATALOG, nId );
        
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_CATALOG, SiteMessage.TYPE_CONFIRMATION, url.getUrl(  ) );
        return null;
    }

    /**
     * Handles the removal form of a catalog
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage catalogs
     */
    @Action( ACTION_REMOVE_CATALOG )
    public XPage doRemoveCatalog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );
        CatalogHome.remove( nId );
        addInfo( INFO_CATALOG_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }

    /**
     * Returns the form to update info about a catalog
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CATALOG )
    public XPage getModifyCatalog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );

        if ( _catalog == null  || ( _catalog.getId( ) != nId ) )
        {
            _catalog = CatalogHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATALOG, _catalog );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_CATALOG ) );

        return getXPage( TEMPLATE_MODIFY_CATALOG, getLocale( request ), model );
    }

    /**
     * Process the change form of a catalog
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_CATALOG )
    public XPage doModifyCatalog( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _catalog, request, getLocale( request ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_CATALOG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _catalog ) )
        {
            return redirect( request, VIEW_MODIFY_CATALOG, PARAMETER_ID_CATALOG, _catalog.getId( ) );
        }

        CatalogHome.update( _catalog );
        addInfo( INFO_CATALOG_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }
}
