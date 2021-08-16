import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import models.Hero;
import models.Squad;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {

        ProcessBuilder process = new ProcessBuilder();
        Integer port;

        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        }else {
            port = 4567;
        }
        port(port);
        staticFileLocation("/public");
        Hero.setUpNewHero();
        Hero.setUpNewHero1();
        Squad.setUpNewSquad();


        get("/index", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/hero-form", (request, response) -> {
            Map<String,Object> model = new HashMap<>();
            return new ModelAndView(model,"hero-form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/hero", (request, response) -> {
            Map<String,Object> model = new HashMap<>();
            ArrayList<Hero> hero = Hero.getAllInstances();
            model.put("hero",hero);
            return new ModelAndView(model, "hero.hbs");
        },  new HandlebarsTemplateEngine());

        get("/new/:id",(req, res) ->{
            Map<String, Object> model = new HashMap<>();
            int idOfHero = Integer.parseInt(req.params(":id"));
            Hero foundHero = Hero.findById(idOfHero);
            model.put("hero",foundHero);
            return new ModelAndView(model, "more.hbs");
        }, new HandlebarsTemplateEngine());

        get("/squad-form",(req, res) ->{
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "squad-form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/",(req, res) ->{
            Map<String, Object> model = new HashMap<>();
            ArrayList<Squad> squads = Squad.getInstances();
            model.put("squads",squads);
            ArrayList<Hero> members = Hero.getAllInstances();
            model.put("heroes",members);
            Squad newSquad = Squad.findBySquadId(1);
            model.put("allSquadMembers", newSquad.getSquadMembers());
            return new ModelAndView(model, "squad.hbs");
        }, new HandlebarsTemplateEngine());


        post("/squad/new", (request, response) -> {
            Map<String,Object> model = new HashMap<>();
            String squadName = request.queryParams("squadName");
            Integer size = Integer.parseInt(request.queryParams("size"));
            String cause = request.queryParams("cause");
            Squad newSquad = new Squad(squadName,size,cause);
            request.session().attribute("item",squadName);
            model.put("item",request.session().attribute("item"));
            return new ModelAndView(model, "success.hbs");
        },  new HandlebarsTemplateEngine());

        post("/new/hero",(req, res) ->{
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Integer age = Integer.parseInt(req.queryParams("age"));
            String power = req.queryParams("power");
            String weakness = req.queryParams("weakness");
            Hero newHero = new Hero(name,age,power,weakness);
            req.session().attribute("item",name);
            model.put("item",req.session().attribute("item"));
            model.put("newHero",newHero);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/new/member/:squadId",(req,res)->{
            Map<String, Object> model = new HashMap<>();
            req.session().attribute("selectedSquad",req.params("squadId"));
            model.put("selectedSquad", req.session().attribute("selectedSquad"));
            model.put("item",1);
            return new ModelAndView(model, "success.hbs");
        },new HandlebarsTemplateEngine());

        get("/squad/new/:id",(req,res)->{
            Map<String, Object> model = new HashMap<>();
            int id= Integer.parseInt(req.params(":id"));
            Hero newMember = Hero.findById(id);
            Squad newSquad = Squad.findBySquadId(1);
            newSquad.setSquadMembers(newMember);
            model.put("item", newMember.getName());
            model.put("newHero",newSquad.getSquadName());
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());



    }

}
