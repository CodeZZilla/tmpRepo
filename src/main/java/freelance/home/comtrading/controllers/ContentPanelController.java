package freelance.home.comtrading.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freelance.home.comtrading.domain.item.Item;
import freelance.home.comtrading.domain.item.TableRecord;
import freelance.home.comtrading.service.CacheService;
import freelance.home.comtrading.service.ConfigService;
import freelance.home.comtrading.service.ItemService;
import freelance.home.comtrading.service.ItemServiceAll;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class ContentPanelController {
    private final ItemService itemService;
    private final ItemServiceAll itemServiceAll;
    private final ObjectMapper mapper = new ObjectMapper();

    public ContentPanelController(ItemService itemService, ItemServiceAll itemServiceAll) {
        this.itemService = itemService;
        this.itemServiceAll = itemServiceAll;
    }

    @CrossOrigin
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/hotline_feed", method = {RequestMethod.GET}, produces = {"application/json"})
    public String hotlineFeed() throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(itemService.getItemsFilteredByStatus("CONFIRMED,GOOD").getContent());
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/loadTable", method = {RequestMethod.GET}, produces = {"application/json"})
    public TableRecord loadTable(@RequestParam(value = "offset") Integer offset,
                                 @RequestParam(value = "limit") Integer limit,
                                 @RequestParam(value = "sort", required = false, defaultValue = "feedId") String sort,
                                 @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                                 @RequestParam(value = "filter", required = false, defaultValue = "{\"parseStatus\":\"CONFIRMED\"}") String filter) throws JsonProcessingException {
        Page<Item> allItems = itemService.findAllItems(offset, limit, sort, order, mapper.readTree(filter).get("parseStatus").asText());

        TableRecord tableRecord = new TableRecord();
        tableRecord.setTotal(allItems.getTotalElements());
        tableRecord.setTotalNotFiltered(allItems.getTotalElements());

        tableRecord.getRows().addAll(allItems.getContent());
        return tableRecord;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateUrl", method = {RequestMethod.GET})
    public ResponseEntity<Object> updateUrl(
            @RequestParam(value = "feedId") Long feedId,
            @RequestParam(value = "url") String url
            ) {
        try {
            itemService.updateUrl(feedId, url);
            return ResponseEntity.status(200).body("{}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{}");
        }
    }

    @RequestMapping(value = "/feed", method = {RequestMethod.GET}, produces = {"application/json"})
    public String feed() throws IOException {
        return Files.readString(Path.of("test.json"));
    }

    @RequestMapping(value = "/getAllData", method = {RequestMethod.GET}, produces = {"application/json"})
    public List<Item> getAllData(){
        return itemServiceAll.findAll();
    }

}
