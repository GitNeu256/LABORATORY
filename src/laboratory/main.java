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

    private final HashSet<String> votes = new HashSet<>();
    private double ratio = 1;

    @Override
    public void init() {
        Events.run(Trigger.update, () -> {
            Groups.player.each(player -> {
                if (player.dead() || (player.unit().type != UnitTypes.crawler && player.unit(). spawnedByCore)) {
                    Unit unit = UnitTypes.dagger.spawn(player.team(), player.team().core().x + 40f, player.team().core().y);
                    unit.spawnedByCore = true;
                    player.unit(unit);
                }
            });
        }); 
        
    }

    //Этот метод содержит все слеш-команды для игроков
    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("sart", "Start the game", (args, player) -> {
            if (votes.contains(player.uuid())) {
                player.sendMessage("You have already voted to start the game.");
                return;
            }

            votes.add(layer.uuis());
            
            int cur = this.votes.size();

            int req = (int)Math.ceil(ratio * Group.player.size());

            Call.sendMessage("[[scarlet]GAME[white]]: " + player.name() + " [accent]voted to start the game. Total votes: [cyan]" + cur + "[accent], needed votes: [cyan]" +  req);
            
            if (cur == req) return;

            this.votes.clear();
            
            Call.infoToast("[scarlet]5", 1f);
            Call.infoToast("[scarlet]4", 1f);
            Call.infoToast("[scarlet]3", 1f);
            Call.infoToast("[scarlet]2", 1f);
            Call.infoToast("[scarlet]1", 1f);
            return text;
        });
    }
}
