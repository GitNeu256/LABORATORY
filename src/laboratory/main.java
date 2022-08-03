package laboratory;

import arc.util.*;
import arc.*;
import arc.graphics.*;

import mindustry.mod.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.net.*;
import mindustry.game.EventType.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.entities.*;

import java.util.*;

public class main extends Plugin {
    Random rand = new Random();

    private final HashSet<String> votes = new HashSet<>();
    private double ratio = 1;
    boolean spawn = true;

    private ArrayList<String> member = new ArrayList<String>();

    @Override
    public void init() {
        Events.run(Trigger.update, () -> {
            Groups.player.each(player -> {
                if (spawn == true) {
                    if (player.dead() || (player.unit().type != UnitTypes.crawler && player.unit().spawnedByCore)) {
                        Unit unit = UnitTypes.crawler.spawn(player.team(), player.team().core().x + 40f, player.team().core().y);
                        unit.spawnedByCore = true;
                        player.unit(unit);
                    }
                }
            });
        }); 
        
        Events.on(PlayerLeave.class, e -> {
            Player player = e.player;

            int cur = this.votes.size();
            int req = (int) Math.ceil(ratio * Groups.player.size());

            if (votes.contains(player.uuid())) {
                votes.remove(player.uuid());
                Call.sendMessage("[[scarlet]GAME[white]]: " + player.name() + " left the server. Total votes: [cyan]" + cur + "[accent], need votes: [cyan]" +  req);
            }
        });

        Events.on(GameOverEvent.class, e -> {
            this.votes.clear();
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("start", "Start the game.", (args, player) -> {
            votes.add(player.uuid());
            member.add(player.uuid());
            int cur = this.votes.size();
            int req = (int) Math.ceil(ratio * Groups.player.size());

            Call.sendMessage("[[scarlet]GAME[white]]: " + player.name() + " [accent]voted to start the game. Total votes: [cyan]" + cur + "[accent], needed votes: [cyan]" +  req);
            
            if (cur < req) {
                return;
            }

            this.votes.clear();

            spawn = false;

            int randomIndex = rand.nextInt(member.size());
            String monster = member.get(randomIndex);

            Player player_monster = Groups.player.find(p -> p.uuid().equals(monster));
            player_monster.team(Team.crux);

            if (player_monster.unit().type != UnitTypes.atrax && player_monster.unit().spawnedByCore) {
                Unit unit = UnitTypes.atrax.spawn(player.team(), 50f, 50f);
                unit.spawnedByCore = false;
                player.unit(unit);
            }

            player_monster.sendMessage("I'm Object-67B");

            Call.infoToast("[red]Game started", 10f);
        });
    }
}
