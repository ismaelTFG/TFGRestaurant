package com.eep.TFGRestaurant.controller;

import com.eep.TFGRestaurant.entity.comandas.ComandasDTO;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosDto;
import com.eep.TFGRestaurant.entity.productos.ProductosEntity;
import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserResponse;
import com.eep.TFGRestaurant.service.ComandasPagadasService;
import com.eep.TFGRestaurant.service.ComandasService;
import com.eep.TFGRestaurant.service.ProductosService;
import com.eep.TFGRestaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
public class Controller {

    //vistas utilizadas en el proyecto
    private static final Html html = new Html();
    //usuario iniciado para el login
    private static UserResponse user = new UserResponse("sinuser", "", false);

    //servicios usados en el controller
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("productosServiceImpl")
    private ProductosService productosService;

    @Autowired
    @Qualifier("comandasServiceImpl")
    private ComandasService comandasService;

    @Autowired
    @Qualifier("comandasPagadasServiceImpl")
    private ComandasPagadasService comandasPagadasService;

    /**
     * vista del login
     * @return login
     */
    @GetMapping("/")
    public ModelAndView index(){

        ModelAndView mav = new ModelAndView(html.index);

        mav.addObject("user", new UserDto());
        mav.addObject("correcto", false);

        return mav;

    }

    /**
     * inicio de sesion
     * @param userDto usuario a iniciar
     * @param model parametro para meter cosas en el inicio
     * @return inicio o login
     */
    @PostMapping("/inicio")
    public String inicio(@ModelAttribute("user") UserDto userDto, Model model){

        if (userService.validar(userService.DtoToEntity(userDto))){

            user = userService.entityToResponse(userService.findByUser(userDto.getUser()));
            model.addAttribute("inicio", user);

            return html.inicio;

        }else {

            model.addAttribute("user", userDto);
            model.addAttribute("correcto", true);

            return html.index;

        }

    }

    /**
     * vista del inicio
     * @return inicio
     */
    @GetMapping("/inicio")
    public ModelAndView ini(){

        ModelAndView mav = new ModelAndView(html.inicio);

        mav.addObject("inicio", user);

        return mav;

    }

    /**
     * vista del añadir usuario
     * @return añadir usuario
     */
    @GetMapping("/addUser")
    public ModelAndView addUser(){

        ModelAndView mav = new ModelAndView(html.addUser);

        mav.addObject("user", new UserDto());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);
        mav.addObject("nouser", false);

        return mav;

    }

    /**
     * añadir un usuario
     * @param userDto usuario a añadir
     * @param result parametro para la validacion
     * @param model parametro para meter cosas a añadir
     * @return añadir usuario
     */
    @PostMapping("/addUser")
    public String anyadirUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("user", userDto);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("nouser", false);

        }else if (userService.userNoRepetidos(userService.DtoToEntity(userDto))){

            model.addAttribute("user", userDto);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("nouser", true);

        }else {

            userService.add(userService.DtoToEntity(userDto));

            model.addAttribute("user", new UserDto());
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);
            model.addAttribute("nouser", false);

        }

        return html.addUser;

    }

    /**
     * vista del eliminar usuario
     * @return eliminar usuario
     */
    @GetMapping("/deleteUser")
    public ModelAndView deleteUser(){

        ModelAndView mav = new ModelAndView(html.deleteUser);

        mav.addObject("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);

        return mav;

    }

    /**
     * eliminar varios usuarios
     * @param seleccionados seleccion de los usuarios
     * @param model parametro para meter cosas a eliminar
     * @return eliminar usuarios
     */
    @PostMapping("/deleteUser")
    public String borrarUser(@RequestParam(value = "seleccion") ArrayList<String> seleccionados, Model model){

        if (userService.manyDelete(seleccionados)){

            model.addAttribute("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);

        }else {

            model.addAttribute("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);

        }

        return html.deleteUser;

    }

    /**
     * vista del modificacion de usuarios
     * @return modificacion usuario
     */
    @GetMapping("/updateUser")
    public ModelAndView updateUser(){

        ModelAndView mav = new ModelAndView(html.updateUser);

        mav.addObject("user", new UserDto());
        mav.addObject("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
        mav.addObject("correcto", false);
        mav.addObject("error", false);
        mav.addObject("inicio", user);

        return mav;

    }

    /**
     * modificar usuario
     * @param userDto usuario a modificar
     * @param result parametro para validacion
     * @param model parametro para meter cosas a modificar
     * @return modificar usuario
     */
    @PostMapping("/updateUser")
    public String modificarUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("user", userDto);
            model.addAttribute("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("inicio", user);

        }else {

            userService.update(userService.DtoToEntity(userDto));

            model.addAttribute("user", new UserDto());
            model.addAttribute("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);
            model.addAttribute("inicio", user);

        }

        return html.updateUser;

    }

    /**
     * vista del añadir producto
     * @return añadir producto
     */
    @GetMapping("/addProductos")
    public ModelAndView addProductos(){

        ModelAndView mav = new ModelAndView(html.addProductos);

        mav.addObject("producto", new ProductosDto());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);
        mav.addObject("noproducto", false);

        return mav;

    }

    /**
     * añadir producto
     * @param productosDto producto a añadir
     * @param result parametro para validacion
     * @param model parametro para meter cosas a añadir
     * @return añadir producto
     */
    @PostMapping("/addProductos")
    public String anyadirproducto(@Valid @ModelAttribute("producto") ProductosDto productosDto, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("producto", productosDto);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("noproducto", false);

        }else if (productosService.productoNoRepetido(productosService.DtoToEntity(productosDto))){

            model.addAttribute("producto", productosDto);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("noproducto", true);

        }else {

            productosService.add(productosService.DtoToEntity(productosDto));

            model.addAttribute("producto", new ProductosDto());
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);
            model.addAttribute("noproducto", false);

        }

        return html.addProductos;

    }

    /**
     * vista del eliminar productos
     * @return eliminar productos
     */
    @GetMapping("/deleteProductos")
    public ModelAndView deleteProducto(){

        ModelAndView mav = new ModelAndView(html.deleteProductos);

        mav.addObject("lista", productosService.listResponseToListEntity(productosService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);

        return mav;

    }

    /**
     * borrar productos
     * @param id seleccion de los productos a eliminar
     * @param model parametro para meter cosas a eliminar
     * @return borrar producto
     */
    @PostMapping("/deleteProductos")
    public String borrarProducto(@RequestParam(value = "seleccion") ArrayList<String> id, Model model){

        if (productosService.manyDelete(id)){

            model.addAttribute("lista", productosService.listResponseToListEntity(productosService.listAll()));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);

        }else {

            model.addAttribute("lista", productosService.listResponseToListEntity(productosService.listAll()));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);

        }

        return html.deleteProductos;

    }

    /**
     * Busqueda de los productos por la categoria para eliminar
     * @param categoria filtro
     * @param model parametro para meter cosas eliminar
     * @return eliminar productos
     */
    @PostMapping("/busquedaproductodelete")
    public String busquedaproducto(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.listResponseToListEntity(productosService.busquedacategoria(categoria)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("error", false);

        return html.deleteProductos;

    }

    /**
     * vista del modificar producto
     * @return modificar producto
     */
    @GetMapping("/updateProductos")
    public ModelAndView updateProducto(){

        ModelAndView mav = new ModelAndView(html.updateProductos);

        mav.addObject("lista", productosService.listResponseToListEntity(productosService.listAll()));
        mav.addObject("inicio", user);

        return mav;

    }

    /**
     * Busqueda de los productos por la categoria para modificar
     * @param categoria categoria a buscar
     * @param model parametro a meter en modificacion
     * @return modificacion
     */
    @PostMapping("/busquedaproducto")
    public String busquedaproductover(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.listResponseToListEntity(productosService.busquedacategoria(categoria)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("error", false);

        return html.updateProductos;

    }

    /**
     * busqueda del producto a modificar
     * @param id id del producto
     * @param model parametro para meter cosas a modificar
     * @return modificar
     */
    @PostMapping("/modificarProductos")
    public String modifProducto(@RequestParam(value = "id") String id, Model model){

        ProductosEntity productos = productosService.findById(id);

        model.addAttribute("producto", new ProductosDto(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio()));
        model.addAttribute("inicio", user);

        return html.modificarProducto;

    }

    /**
     * modificar un producto
     * @param productosDto producto a modificar
     * @param result parametro para la validacion
     * @param model parametro para meter cosas en modificar
     * @return modificar
     */
    @PostMapping("/updateProductos")
    public String modificarProducto(@Valid @ModelAttribute("producto") ProductosDto productosDto, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("lista", productosService.listResponseToListEntity(productosService.listAll()));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);

            return html.updateProductos;

        }else {

            productosService.update(productosService.DtoToEntity(productosDto));

            model.addAttribute("lista", productosService.listResponseToListEntity(productosService.listAll()));
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);

            return html.updateProductos;

        }

    }

    /**
     * vista del añadir comanda
     * @return añadir
     */
    @GetMapping("/addComandas")
    public ModelAndView addComandas(){

        ModelAndView mav = new ModelAndView(html.addComandas);

        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);
        mav.addObject("nomesa", false);

        return mav;

    }

    /**
     * añadir comanda
     * @param comandasDTO comanda a añadir
     * @param result parametro para la validacion
     * @param model parametro para meter cosas a añadir
     * @return añadir
     */
    @PostMapping("/addComandas")
    public String anyadirComandas(@Valid @ModelAttribute("comanda") ComandasDTO comandasDTO, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("comanda", comandasDTO);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("nomesa", false);

        }else if (comandasService.mesaNoRepetida(comandasService.DtoToEntity(comandasDTO))){

            model.addAttribute("comanda", comandasDTO);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("nomesa", true);

        }else {

            comandasService.add(comandasService.DtoToEntity(comandasDTO));

            model.addAttribute("comanda", comandasDTO);
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);
            model.addAttribute("nomesa", false);

        }

        return html.addComandas;

    }

    /**
     * vista de modificar comanda y añadir productos a comanda
     * @return modificar
     */
    @GetMapping("/updateComandas")
    public ModelAndView updateComandas(){

        ModelAndView mav = new ModelAndView(html.updateComanda);

        mav.addObject("listacomandas", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("lista", productosService.listResponseToListEntity(productosService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);

        return mav;

    }

    /**
     * modificacion comanda
     * @param comandasDTO comanda a modificar
     * @param seleccionados productos seleccionados
     * @param model parametro para meter cosas a modificar
     * @return modificar
     */
    @PostMapping("/updateComanda")
    public String modificarComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, @RequestParam(value = "seleccion") ArrayList<String> seleccionados, Model model){

        comandasService.addProductos(comandasDTO, seleccionados);

        model.addAttribute("listacomandas", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("lista", productosService.listResponseToListEntity(productosService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);

        return html.updateComanda;

    }

    /**
     * vista del eliminar productos en comanda
     * @return eliminar
     */
    @GetMapping("/deleteProductosComanda")
    public ModelAndView deleteProductosComanda(){

        ModelAndView mav = new ModelAndView(html.deleteProductosComandas);

        mav.addObject("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);

        return mav;

    }

    /**
     * busqueda de comanda en eliminar producto
     * @param comandasDTO parametro a buscar
     * @param model parametros para meter cosas en eliminar
     * @return eliminar
     */
    @PostMapping("/buscarComanda")
    public String buscarComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        model.addAttribute("comanda", comandasDTO);
        model.addAttribute("lista", comandasService.productos(comandasService.findByMesa(comandasDTO.getMesa())));
        model.addAttribute("inicio", user);

        return html.dpc;

    }

    /**
     * eliminar productos en comanda
     * @param comandasDTO comanda
     * @param seleccionados productos a eliminar
     * @param model parametro para meter cosas a eliminar
     * @return eliminar
     */
    @PostMapping("/deleteProductosComanda")
    public String eliminarProductosComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, @RequestParam(value = "seleccion") ArrayList<Integer> seleccionados, Model model){

        comandasService.deleteProductos(comandasDTO, seleccionados);

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);

        return html.deleteProductosComandas;

    }

    /**
     * vista de eliminar comanda
     * @return eliminar
     */
    @GetMapping("/deleteComandas")
    public ModelAndView deleteComandas(){

        ModelAndView mav = new ModelAndView(html.deleteComandas);

        mav.addObject("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("listamesa", comandasService.mesa(comandasService.listAll()));
        mav.addObject("listacamarero", comandasService.camarero(comandasService.listAll()));

        return mav;

    }

    /**
     * busqueda de comanda en eliminar comanda
     * @param comandasDTO parametros de busqueda
     * @param model parametro para meter cosas en eliminar
     * @return eliminar
     */
    @PostMapping("/filtroComandaDelete")
    public String filtroComandaDelete(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        model.addAttribute("lista", comandasService.filtro(comandasService.DtoToEntity(comandasDTO)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("comanda", comandasDTO);
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.deleteComandas;

    }

    /**
     * eliminar comanda
     * @param mesa comandas a eliminar
     * @param model parametro para meter cosas a eliminar
     * @return eliminar
     */
    @PostMapping("/deleteComandas")
    public String eliminarComandas(@RequestParam("seleccion") ArrayList<Integer> mesa, Model model){

        comandasService.manyDelete(mesa);

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.deleteComandas;

    }

    /**
     * vista de comanda ticket
     * @return ticket
     */
    @GetMapping("/pagarComanda")
    public ModelAndView pagarComanda(){

        ModelAndView mav = new ModelAndView(html.pagarComandas);

        mav.addObject("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("filtro", new ComandasDTO());
        mav.addObject("listamesa", comandasService.mesa(comandasService.listAll()));
        mav.addObject("listacamarero", comandasService.camarero(comandasService.listAll()));

        return mav;

    }

    /**
     * busqueda de comanda en ticket
     * @param comandasDTO parametros de busqueda
     * @param model parametro para meter cosas a ticket
     * @return ticket
     */
    @PostMapping("/filtroComandaTicket")
    public String filtroComandaTicket(@ModelAttribute("filtro") ComandasDTO comandasDTO, Model model){

        model.addAttribute("lista", comandasService.filtro(comandasService.DtoToEntity(comandasDTO)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("filtro", comandasDTO);
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.pagarComandas;

    }

    /**
     * generar el ticket
     * @param comandasDTO comanda
     * @param model parametro para meter cosas en ticket
     * @return ticket
     */
    @PostMapping("/pagarComanda")
    public String ticket(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        comandasService.pagar(comandasService.findByMesa(comandasDTO.getMesa()));
        comandasService.imprimir();

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("filtro", new ComandasDTO());
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.pagarComandas;

    }

    /**
     * vista de comanda pagada
     * @return comanda pagada
     */
    @GetMapping("/pagadaComanda")
    public ModelAndView comandaPagada(){

        ModelAndView mav = new ModelAndView(html.comandaPagada);

        mav.addObject("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("filtro", new ComandasDTO());
        mav.addObject("listamesa", comandasService.mesa(comandasService.listAll()));
        mav.addObject("listacamarero", comandasService.camarero(comandasService.listAll()));

        return mav;

    }

    /**
     * busqueda de comanda en comanda pagada
     * @param comandasDTO parametros de busqueda
     * @param model parametro para meter cosas a comanda pagada
     * @return comanda pagada
     */
    @PostMapping("/filtroComandaPagada")
    public String filtroComandaPagada(@ModelAttribute("filtro") ComandasDTO comandasDTO, Model model){

        model.addAttribute("lista", comandasService.filtro(comandasService.DtoToEntity(comandasDTO)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("filtro", comandasDTO);
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.comandaPagada;

    }

    /**
     * comanda pagada
     * @param comandasDTO comanda
     * @param model parametro para meter cosas en comanda pagada
     * @return comanda pagada
     */
    @PostMapping("/pagadaComanda")
    public String addComandaPagada(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        comandasService.pagada(comandasService.findByMesa(comandasDTO.getMesa()));

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("filtro", new ComandasDTO());
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.comandaPagada;

    }

    /**
     * vista de comandas antiguas
     * @return comandas antiguas
     */
    @GetMapping("/verComandasAntiguas")
    public ModelAndView verComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.ComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listEntityToListResponse(comandasPagadasService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("filtro", new ComandasPagadasDto());
        mav.addObject("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        mav.addObject("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));

        return mav;

    }

    /**
     * busqueda de comandas antiguas
     * @param comandasPagadasDto parametros de busqueda
     * @param model parametro para meter cosas a comandas antiguas
     * @return comanda antiguas
     */
    @PostMapping("/filtroComandaAntiguas")
    public String filtroComandaAntiguas(@ModelAttribute("filtro") ComandasPagadasDto comandasPagadasDto, Model model){

        model.addAttribute("lista", comandasPagadasService.filtro(comandasPagadasDto));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));

        return html.ComandasAntiguas;

    }

    /**
     * vista de eliminar comandas antiguas
     * @return eliminar
     */
    @GetMapping("/deleteComandasAntiguas")
    public ModelAndView deleteComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.deleteComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listEntityToListResponse(comandasPagadasService.listAll()));
        mav.addObject("filtro", new ComandasPagadasDto());
        mav.addObject("inicio", user);
        mav.addObject("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        mav.addObject("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        mav.addObject("correcto", false);

        return mav;

    }

    /**
     * busqueda de eliminar comandas antiguas
     * @param comandasPagadasDto parametros de busqueda
     * @param model parametro para meter cosas a comandas antiguas
     * @return eliminar comanda antiguas
     */
    @PostMapping("/filtroComandaAntiguasDelete")
    public String filtroComandaAntiguasDelete(@ModelAttribute("filtro") ComandasPagadasDto comandasPagadasDto, Model model){

        model.addAttribute("lista", comandasPagadasService.filtro(comandasPagadasDto));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        model.addAttribute("correcto", false);

        return html.deleteComandasAntiguas;

    }

    /**
     * eliminar comandas antiguas
     * @param id comandas a eliminar
     * @param model parametro para meter cosas a eliminar
     * @return eliminar
     */
    @PostMapping("/deleteComandasAntiguas")
    public String eliminarComandasAntiguas(@RequestParam(value = "seleccion") ArrayList<String> id, Model model){

        comandasPagadasService.manyDelete(id);

        model.addAttribute("lista", comandasPagadasService.listEntityToListResponse(comandasPagadasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        model.addAttribute("correcto", true);

        return html.deleteComandasAntiguas;

    }

    /**
     * vista de modificacion comandas antiguas
     * @return modificar
     */
    @GetMapping("/updateComandasAntiguas")
    public ModelAndView updateComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.updateComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listEntityToListResponse(comandasPagadasService.listAll()));
        mav.addObject("comanda", new ComandasPagadasDto());
        mav.addObject("filtro", new ComandasPagadasDto());
        mav.addObject("inicio", user);
        mav.addObject("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        mav.addObject("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        mav.addObject("correcto", false);

        return mav;

    }

    /**
     * busqueda de modificacion comandas antiguas
     * @param comandasPagadasDto parametros de busqueda
     * @param model parametro para meter cosas a comandas antiguas
     * @return modificacion
     */
    @PostMapping("/filtroComandaAntiguasUpdate")
    public String filtroComandaAntiguasUpdate(@ModelAttribute("filtro") ComandasPagadasDto comandasPagadasDto, Model model){

        model.addAttribute("lista", comandasPagadasService.filtro(comandasPagadasDto));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("comanda", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        model.addAttribute("correcto", false);

        return html.updateComandasAntiguas;

    }

    /**
     * modificacion de comanda antiguas
     * @param comandasPagadasDto comandas
     * @param model parametro para meter cosa a modificar
     * @return modificar
     */
    @PostMapping("/updateComandasAntiguas")
    public String modificarComandaAntiguas(@ModelAttribute("comanda") ComandasPagadasDto comandasPagadasDto, Model model){

        ComandasPagadasEntity comandasPagadasEntity = comandasPagadasService.findById(comandasPagadasDto.getId());

        comandasPagadasEntity.setCamarero(comandasPagadasDto.getCamarero());
        comandasPagadasService.update(comandasPagadasEntity);

        model.addAttribute("lista", comandasPagadasService.listEntityToListResponse(comandasPagadasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("comanda", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));
        model.addAttribute("correcto", true);

        return html.updateComandasAntiguas;

        //linea de codigo 1000 uwu
    }

}
