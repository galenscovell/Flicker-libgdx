package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.google.gson.*;
import galenscovell.things.entities.Monster;

import java.io.*;
import java.util.*;

public class MonsterParser {
    private List<Monster> createMonsterList() {
        List<Monster> monsterList = new ArrayList<Monster>();
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            BufferedReader reader = Gdx.files.internal("monsters.json").reader(1024);
            JsonArray mArray = parser.parse(reader).getAsJsonArray();
            for (JsonElement mElement : mArray) {
                Monster monster = gson.fromJson(mElement, Monster.class);
                monsterList.add(monster);
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return monsterList;
    }

    public Monster spawn(int level) {
        List<Monster> monsterList = this.createMonsterList();
        Random random = new Random();
        while (true) {
            int selection = random.nextInt(monsterList.size());
            Monster selected = monsterList.get(selection);
            if (selected.level == level || selected.level == level - 1) {
                selected.setup();
                return selected;
            }
        }
    }
}
