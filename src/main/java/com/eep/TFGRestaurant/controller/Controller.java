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

    private static final Html html = new Html();
    private static UserResponse user = new UserResponse("sinuser", "", false);

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

    @GetMapping("/")
    public ModelAndView index(){

        ModelAndView mav = new ModelAndView(html.index);

        mav.addObject("user", new UserDto());
        mav.addObject("correcto", false);

        return mav;

    }

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

    @GetMapping("/inicio")
    public ModelAndView ini(){

        ModelAndView mav = new ModelAndView(html.inicio);

        mav.addObject("inicio", user);

        return mav;

    }

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

    @GetMapping("/deleteUser")
    public ModelAndView deleteUser(){

        ModelAndView mav = new ModelAndView(html.deleteUser);

        mav.addObject("lista", userService.listEntityToListRespnse(userService.listAllSinUser(user)));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);

        return mav;

    }

    @PostMapping("/deleteUser")
    public String borrarUser(@RequestParam(value = "seleccion") ArrayList<String> seleccionados, Model model) throws ExecutionException, InterruptedException {

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

    @GetMapping("/deleteProductos")
    public ModelAndView deleteProducto(){

        ModelAndView mav = new ModelAndView(html.deleteProductos);

        mav.addObject("lista", productosService.listResponseToListEntity(productosService.listAll()));
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);

        return mav;

    }

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

    @PostMapping("/busquedaproductodelete")
    public String busquedaproducto(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.listResponseToListEntity(productosService.busquedacategoria(categoria)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("error", false);

        return html.deleteProductos;

    }

    @GetMapping("/updateProductos")
    public ModelAndView updateProducto(){

        ModelAndView mav = new ModelAndView(html.updateProductos);

        mav.addObject("lista", productosService.listResponseToListEntity(productosService.listAll()));
        mav.addObject("inicio", user);

        return mav;

    }

    @PostMapping("/busquedaproducto")
    public String busquedaproductover(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.listResponseToListEntity(productosService.busquedacategoria(categoria)));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("error", false);

        return html.updateProductos;

    }

    @PostMapping("/modificarProductos")
    public String modifProducto(@RequestParam(value = "id") String id, Model model){

        ProductosEntity productos = productosService.findById(id);

        model.addAttribute("producto", new ProductosDto(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio()));
        model.addAttribute("inicio", user);

        return html.modificarProducto;

    }

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

    @GetMapping("/deleteProductosComanda")
    public ModelAndView deleteProductosComanda(){

        ModelAndView mav = new ModelAndView(html.deleteProductosComandas);

        mav.addObject("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);

        return mav;

    }

    @PostMapping("/buscarComanda")
    public String buscarComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        model.addAttribute("comanda", comandasDTO);
        model.addAttribute("lista", comandasService.productos(comandasService.findByMesa(comandasDTO.getMesa())));
        model.addAttribute("inicio", user);

        return html.dpc;

    }

    @PostMapping("/deleteProductosComanda")
    public String eliminarProductosComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, @RequestParam(value = "seleccion") ArrayList<Integer> seleccionados, Model model){

        comandasService.deleteProductos(comandasDTO, seleccionados);

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);

        return html.deleteProductosComandas;

    }

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

    @PostMapping("/pagarComanda")
    public String ticket(@ModelAttribute("comanda") ComandasDTO comandasDTO, Model model){

        comandasService.pagar(comandasService.findByMesa(comandasDTO.getMesa()));

        model.addAttribute("lista", comandasService.listEntityToListResponse(comandasService.listAll()));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", true);
        model.addAttribute("comanda", new ComandasDTO());
        model.addAttribute("filtro", new ComandasDTO());
        model.addAttribute("listamesa", comandasService.mesa(comandasService.listAll()));
        model.addAttribute("listacamarero", comandasService.camarero(comandasService.listAll()));

        return html.pagarComandas;

    }

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

    @PostMapping("/filtroComandaAntiguas")
    public String filtroComandaAntiguas(@ModelAttribute("filtro") ComandasPagadasDto comandasPagadasDto, Model model){

        model.addAttribute("lista", comandasPagadasService.filtro(comandasPagadasDto));
        model.addAttribute("inicio", user);
        model.addAttribute("filtro", new ComandasPagadasDto());
        model.addAttribute("listamesa", comandasPagadasService.mesa(comandasPagadasService.listAll()));
        model.addAttribute("listacamarero", comandasPagadasService.camarero(comandasPagadasService.listAll()));

        return html.ComandasAntiguas;

    }

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

    }

}
