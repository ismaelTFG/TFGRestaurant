package com.eep.TFGRestaurant.controller;

import com.eep.TFGRestaurant.entity.productos.ProductosDto;
import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.service.ProductosService;
import com.eep.TFGRestaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
public class Controller {

    Html html = new Html();

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("productosServiceImpl")
    private ProductosService productosService;

    @GetMapping("/")
    public ModelAndView index(){

        ModelAndView mav = new ModelAndView(html.index);

        mav.addObject("user", new UserDto());

        return mav;

    }

    @PostMapping("/inicio")
    public String inicio(@ModelAttribute("user") UserDto userDto){

        if (userService.validar(userService.DtoToEntity(userDto))){

            return html.inicio;

        }else {

            return html.index;

        }

    }

    @GetMapping("/user")
    public ModelAndView user(){

        ModelAndView mav = new ModelAndView(html.user);

        return mav;

    }

    @GetMapping("/productos")
    public ModelAndView productos(){

        ModelAndView mav = new ModelAndView(html.productos);

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

}
