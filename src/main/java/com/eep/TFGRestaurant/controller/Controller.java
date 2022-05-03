package com.eep.TFGRestaurant.controller;

import com.eep.TFGRestaurant.entity.comandas.ComandasDTO;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosDto;
import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserResponse;
import com.eep.TFGRestaurant.service.ComandasPagadasService;
import com.eep.TFGRestaurant.service.ComandasService;
import com.eep.TFGRestaurant.service.ProductosService;
import com.eep.TFGRestaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Html html = new Html();
    private static UserResponse sinuser = new UserResponse("sinuser", "", false);

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

        return mav;

    }

    @PostMapping("/inicio")
    public String inicio(@ModelAttribute("user") UserDto userDto, Model model){

        if (userService.validar(userService.DtoToEntity(userDto))){

            sinuser = userService.entityToResponse(userService.findByUser(userDto.getUser()));
            model.addAttribute("inicio", sinuser);

            return html.inicio;

        }else {

            return html.index;

        }

    }

    @GetMapping("/inicio")
    public ModelAndView ini(){

        ModelAndView mav = new ModelAndView(html.inicio);

        mav.addObject("inicio", sinuser);

        return mav;

    }

    @GetMapping("/addUser")
    public ModelAndView addUser(){

        ModelAndView mav = new ModelAndView(html.addUser);

        mav.addObject("user", new UserDto());

        return mav;

    }

    @PostMapping("/addUser")
    public String anyadirUser(@ModelAttribute("user") UserDto userDto){

        userService.add(userService.DtoToEntity(userDto));

        return html.inicio;

    }

    @GetMapping("/deleteUser")
    public ModelAndView deleteUser() throws ExecutionException, InterruptedException {

        ModelAndView mav = new ModelAndView(html.deleteUser);

        mav.addObject("nombre", new UserDto());
        mav.addObject("lista", userService.listAll());

        return mav;

    }

    @PostMapping("/deleteUser")
    public String borrarUser(@ModelAttribute("nombre") UserDto id){

        userService.delete(id.getUser());

        return html.inicio;

    }

    @GetMapping("/updateUser")
    public ModelAndView updateUser() throws ExecutionException, InterruptedException {

        ModelAndView mav = new ModelAndView(html.updateUser);

        mav.addObject("user", new UserDto());
        mav.addObject("lista", userService.listAll());

        return mav;

    }

    @PostMapping("/updateUser")
    public String modificarUser(@ModelAttribute("user") UserDto userDto){

        userService.update(userService.DtoToEntity(userDto));

        return html.inicio;

    }

    @GetMapping("/addProductos")
    public ModelAndView addProductos(){

        ModelAndView mav = new ModelAndView(html.addProductos);

        mav.addObject("producto", new ProductosDto());

        return mav;

    }

    @PostMapping("/addProductos")
    public String anyadirproducto(@ModelAttribute("producto") ProductosDto productosDto){

        productosService.add(productosService.DtoToEntity(productosDto));

        return html.inicio;

    }

    @GetMapping("/deleteProductos")
    public ModelAndView deleteProducto(){

        ModelAndView mav = new ModelAndView(html.deleteProductos);

        mav.addObject("lista", productosService.listAll());
        mav.addObject("producto", new ProductosDto());

        return mav;

    }

    @PostMapping("/deleteProductos")
    public String borrarProducto(@ModelAttribute("producto") ProductosDto productosDto){

        productosService.delete(productosDto.getId());

        return html.inicio;

    }

    @GetMapping("/updateProductos")
    public ModelAndView updateProducto(){

        ModelAndView mav = new ModelAndView(html.updateProductos);

        mav.addObject("producto", new ProductosDto());
        mav.addObject("lista", productosService.listAll());

        return mav;

    }

    @PostMapping("/updateProductos")
    public String modificarProducto(@ModelAttribute("producto") ProductosDto productosDto){

        productosService.update(productosService.DtoToEntity(productosDto));

        return html.inicio;

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
