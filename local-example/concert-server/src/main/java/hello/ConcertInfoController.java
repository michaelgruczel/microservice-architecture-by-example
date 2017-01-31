package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// e.g. http://localhost:8090/weather?place=springfield
@RestController
public class ConcertInfoController {

    private RestTemplate restTemplate;

    public ConcertInfoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/concerts")
    public ConcertInfo weather(@RequestParam(value="place", defaultValue="") String place) {
        if(place.equalsIgnoreCase("hamburg")) {

            ConcertInfo result = new ConcertInfo("There is always cool stuff in Hamburg");

            try {
                Weather weather = restTemplate.getForObject("http://localhost:8090/weather?place=" + place, Weather.class);
                result.setContent(result.getContent() + " - Weather:" + weather.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;

        } else if(place.equalsIgnoreCase("springfield")) {
            ConcertInfo result = new ConcertInfo("Clowns band on central park");

            try {
                Weather weather = restTemplate.getForObject("http://localhost:8090/weather?place=" + place, Weather.class);
                result.setContent(result.getContent() + " - Weather:" + weather.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;

        } else if(!place.isEmpty()) {
            return new ConcertInfo("that town is unknown, we know hamburg and springfield");
        }
        return new ConcertInfo("In hamburg is always cool stuff, in springfield, The clowns band plays in central park");
    }

}
