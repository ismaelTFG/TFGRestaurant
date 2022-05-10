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
        mav.addObject("lista", userService.listAllSinUser(user));
        mav.addObject("correcto", false);
        mav.addObject("error", false);
        mav.addObject("inicio", user);

        return mav;

    }

    @PostMapping("/updateUser")
    public String modificarUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){

        if (result.hasErrors()){

            model.addAttribute("user", userDto);
            model.addAttribute("lista", userService.listAllSinUser(user));
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);
            model.addAttribute("inicio", user);

        }else {

            userService.update(userService.DtoToEntity(userDto));

            model.addAttribute("user", new UserDto());
            model.addAttribute("lista", userService.listAllSinUser(user));
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

        mav.addObject("lista", productosService.listAll());
        mav.addObject("inicio", user);
        mav.addObject("correcto", false);
        mav.addObject("error", false);

        return mav;

    }

    @PostMapping("/deleteProductos")
    public String borrarProducto(@RequestParam(value = "seleccion") ArrayList<String> id, Model model){

        if (productosService.manyDelete(id)){

            model.addAttribute("lista", productosService.listAll());
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", true);
            model.addAttribute("error", false);

        }else {

            model.addAttribute("lista", productosService.listAll());
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);

        }

        return html.deleteProductos;

    }

    @PostMapping("/busquedaproductodelete")
    public String busquedaproducto(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.busquedacategoria(categoria));
        model.addAttribute("inicio", user);
        model.addAttribute("correcto", false);
        model.addAttribute("error", false);

        return html.deleteProductos;

    }

    @GetMapping("/updateProductos")
    public ModelAndView updateProducto(){

        ModelAndView mav = new ModelAndView(html.updateProductos);

        mav.addObject("lista", productosService.listAll());
        mav.addObject("inicio", user);

        return mav;

    }

    @PostMapping("/busquedaproducto")
    public String busquedaproductover(@RequestParam(value = "categoria") String categoria, Model model){

        model.addAttribute("lista", productosService.busquedacategoria(categoria));
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

            model.addAttribute("lista", productosService.listAll());
            model.addAttribute("inicio", user);
            model.addAttribute("correcto", false);
            model.addAttribute("error", true);

            return html.updateProductos;

        }else {

            productosService.update(productosService.DtoToEntity(productosDto));

            model.addAttribute("lista", productosService.listAll());
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

        return mav;

    }

    @PostMapping("/addComandas")
    public String anyadirComandas(@ModelAttribute("comanda") ComandasDTO comandasDTO){

        comandasService.add(comandasService.DtoToEntity(comandasDTO));

        return html.inicio;

    }

    @GetMapping("/updateComandas")
    public ModelAndView updateComandas(){

        ModelAndView mav = new ModelAndView(html.updateComanda);

        mav.addObject("listacomandas", comandasService.listAll());
        mav.addObject("comanda", new ComandasDTO());
        mav.addObject("lista", productosService.listAll());

        return mav;

    }

    @PostMapping("/updateComanda")
    public String modificarComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, @RequestParam(value = "seleccion") ArrayList<String> seleccionados){

        comandasService.addProductos(comandasDTO, seleccionados);

        return html.inicio;

    }

    @GetMapping("/deleteProductosComanda")
    public ModelAndView deleteProductosComanda(){

        ModelAndView mav = new ModelAndView(html.deleteProductosComandas);

        mav.addObject("lista", comandasService.listAll());
        mav.addObject("comanda", new ComandasDTO());

        return mav;

    }

    @PostMapping("/deleteProductosComanda")
    public String eliminarProductosComanda(@ModelAttribute("comanda") ComandasDTO comandasDTO, @RequestParam(value = "seleccion") ArrayList<Integer> seleccionados){

        comandasService.deleteProductos(comandasDTO, seleccionados);

        return html.inicio;

    }

    @GetMapping("/deleteComandas")
    public ModelAndView deleteComandas(){

        ModelAndView mav = new ModelAndView(html.deleteComandas);

        mav.addObject("lista", comandasService.listAll());
        mav.addObject("comanda", new ComandasDTO());

        return mav;

    }

    @PostMapping("/deleteComandas")
    public String eliminarComandas(@ModelAttribute("comanda") ComandasDTO comandasDTO){

        comandasService.delete(comandasDTO.getMesa());

        return html.inicio;

    }

    @GetMapping("/pagarComanda")
    public ModelAndView pagarComanda(){

        ModelAndView mav = new ModelAndView(html.pagarComandas);

        mav.addObject("lista", comandasService.listAll());
        mav.addObject("comanda", new ComandasDTO());

        return mav;

    }

    @PostMapping("/pagarComanda")
    public String ticket(@ModelAttribute("comanda") ComandasDTO comandasDTO){

        comandasService.pagar(comandasService.findByMesa(comandasDTO.getMesa()));

        return html.inicio;

    }

    @GetMapping("/pagadaComanda")
    public ModelAndView comandaPagada(){

        ModelAndView mav = new ModelAndView(html.comandaPagada);

        mav.addObject("lista", comandasService.listAll());
        mav.addObject("comanda", new ComandasDTO());

        return mav;

    }

    @PostMapping("/pagadaComanda")
    public String addComandaPagada(@ModelAttribute("comanda") ComandasDTO comandasDTO){

        comandasService.pagada(comandasService.findByMesa(comandasDTO.getMesa()));

        return html.inicio;

    }

    @GetMapping("/verComandasAntiguas")
    public ModelAndView verComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.ComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listAll());

        return mav;

    }

    @GetMapping("/deleteComandasAntiguas")
    public ModelAndView deleteComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.deleteComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listAll());
        mav.addObject("comanda", new ComandasPagadasDto());

        return mav;

    }

    @PostMapping("/deleteComandasAntiguas")
    public String eliminarComandasAntiguas(@ModelAttribute("comanda") ComandasPagadasDto comandasPagadasDto){

        comandasPagadasService.delete(comandasPagadasDto.getId());

        return html.inicio;

    }

    @GetMapping("/updateComandasAntiguas")
    public ModelAndView updateComandasAntiguas(){

        ModelAndView mav = new ModelAndView(html.updateComandasAntiguas);

        mav.addObject("lista", comandasPagadasService.listAll());
        mav.addObject("comanda", new ComandasPagadasDto());

        return mav;

    }

    @PostMapping("/updateComandasAntiguas")
    public String modificarComandaAntiguas(@ModelAttribute("comanda") ComandasPagadasDto comandasPagadasDto){

        ComandasPagadasEntity comandasPagadasEntity = comandasPagadasService.findById(comandasPagadasDto.getId());

        comandasPagadasEntity.setCamarero(comandasPagadasDto.getCamarero());
        comandasPagadasService.update(comandasPagadasEntity);

        return html.inicio;

    }

}
