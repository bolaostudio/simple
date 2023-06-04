package com.nova.simple.services;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.nova.simple.R;

@TargetApi(24)
public class ActionService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        // Acciones cuando se añade el servicio de configuración rápida
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        // Acciones cuando se elimina el servicio de configuración rápida
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        // Configuración del servicio de configuración rápida

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Tile tile = getQsTile();
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_stat_logo_app));
            tile.setContentDescription("Botón de SIMple");
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    @Override
    public void onClick() {
        super.onClick();
        // Acción al hacer clic en el servicio de configuración rápida
    }

    /*
    public void onTileLongClick() {
        super.onTileLongClick();
        // Acciones adicionales al mantener presionado el servicio de configuración rápida
    }*/
}
