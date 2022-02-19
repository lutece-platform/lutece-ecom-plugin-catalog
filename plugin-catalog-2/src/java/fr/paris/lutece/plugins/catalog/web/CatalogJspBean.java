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

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.catalog.business.Catalog;
import fr.paris.lutece.plugins.catalog.business.CatalogHome;

/**
 * This class provides the user interface to manage Catalog features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCatalogs.jsp", controllerPath = "jsp/admin/plugins/catalog/", right = "CATALOG_MANAGEMENT" )
public class CatalogJspBean extends AbstractUpdateJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_CATALOGS = "/admin/plugins/catalog/manage_catalogs.html";
    private static final String TEMPLATE_CREATE_CATALOG = "/admin/plugins/catalog/create_catalog.html";
    private static final String TEMPLATE_MODIFY_CATALOG = "/admin/plugins/catalog/modify_catalog.html";

    // Parameters
    private static final String PARAMETER_ID_CATALOG = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CATALOGS = "catalog.manage_catalogs.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CATALOG = "catalog.modify_catalog.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CATALOG = "catalog.create_catalog.pageTitle";

    // Markers
    private static final String MARK_CATALOG_LIST = "catalog_list";
    private static final String MARK_CATALOG = "catalog";

    private static final String JSP_MANAGE_CATALOGS = "jsp/admin/plugins/catalog/ManageCatalogs.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CATALOG = "catalog.message.confirmRemoveCatalog";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "catalog.model.entity.catalog.attribute.";

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
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CATALOGS, defaultView = true )
    public String getManageCatalogs( HttpServletRequest request )
    {
        _catalog = null;
        List<Catalog> listCatalogs = CatalogHome.getCatalogsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CATALOG_LIST, listCatalogs, JSP_MANAGE_CATALOGS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CATALOGS, TEMPLATE_MANAGE_CATALOGS, model );
    }

    /**
     * Returns the form to create a catalog
     *
     * @param request The Http request
     * @return the html code of the catalog form
     */
    @View( VIEW_CREATE_CATALOG )
    public String getCreateCatalog( HttpServletRequest request )
    {
        _catalog = ( _catalog != null ) ? _catalog : new Catalog(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATALOG, _catalog );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_CATALOG ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CATALOG, TEMPLATE_CREATE_CATALOG, model );
    }

    /**
     * Process the data capture form of a new catalog
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_CATALOG )
    public String doCreateCatalog( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _catalog, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_CATALOG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _catalog, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CATALOG );
        }

        CatalogHome.create( _catalog );
        addInfo( INFO_CATALOG_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }

    /**
     * Manages the removal form of a catalog whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CATALOG )
    public String getConfirmRemoveCatalog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CATALOG ) );
        url.addParameter( PARAMETER_ID_CATALOG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CATALOG, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a catalog
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage catalogs
     */
    @Action( ACTION_REMOVE_CATALOG )
    public String doRemoveCatalog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );
        CatalogHome.remove( nId );
        addInfo( INFO_CATALOG_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }

    /**
     * Returns the form to update info about a catalog
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CATALOG )
    public String getModifyCatalog( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CATALOG ) );

        if ( _catalog == null || ( _catalog.getId(  ) != nId ) )
        {
            _catalog = CatalogHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CATALOG, _catalog );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_CATALOG ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CATALOG, TEMPLATE_MODIFY_CATALOG, model );
    }

    /**
     * Process the change form of a catalog
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_CATALOG )
    public String doModifyCatalog( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _catalog, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_CATALOG ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _catalog, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CATALOG, PARAMETER_ID_CATALOG, _catalog.getId( ) );
        }

        CatalogHome.update( _catalog );
        addInfo( INFO_CATALOG_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CATALOGS );
    }
}
