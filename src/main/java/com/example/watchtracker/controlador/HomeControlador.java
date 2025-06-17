@Controller
public class HomeControlador {

    @GetMapping("/")
    @ResponseBody
    public String testLanding() {
        return "¡Landing cargada correctamente!";
    }
}
