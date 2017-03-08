package com.example.android.hpcounter;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int warriorHP;
    int priestHP;
    int warriorArmor;
    int priestArmor;
    boolean warriorsTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValues();
    }

    private void displayForWarrior(int hp, int armor) {
        ((TextView) findViewById(R.id.warrior_hp)).setText(String.valueOf(hp));
        ((TextView) findViewById(R.id.warrior_armor)).setText(String.valueOf(armor));
    }

    private void displayForPriest(int hp, int armor) {
        ((TextView) findViewById(R.id.priest_hp)).setText(String.valueOf(hp));
        ((TextView) findViewById(R.id.priest_armor)).setText(String.valueOf(armor));
    }

    private void initValues() {
        warriorHP = priestHP = 30;
        warriorArmor = priestArmor = 0;
        warriorsTurn = (new Random()).nextBoolean();
        nextTurn();
    }

    public void newGame(View v) {
        initValues();
        displayForWarrior(warriorHP, warriorArmor);
        displayForPriest(priestHP, priestArmor);
    }

    public void priestAttack(View v) {
        // deal from 1 to 5 dmg
        Random rn = new Random();
        int strike = rn.nextInt(5) + 1;
        dealDamage("Warrior", strike);
        Toast.makeText(this, "You deal " + strike + " damage to Warrior!", Toast.LENGTH_SHORT).show();
        findViewById(R.id.priest_attack).setEnabled(false);
    }

    public void warriorAttack(View v) {
        // deal from 1 to 5 dmg
        Random rn = new Random();
        int strike = rn.nextInt(5) + 1;
        dealDamage("Priest", strike);
        Toast.makeText(this, "You deal " + strike + " damage to Priest!", Toast.LENGTH_SHORT).show();
        findViewById(R.id.warrior_attack).setEnabled(false);
    }

    private void dealDamage(String target, int amount) {

        if (target.equals("Warrior")) {

            if (amount > warriorArmor) {
                warriorHP = warriorHP - (amount - warriorArmor);
                warriorArmor = 0;
            } else {
                warriorArmor = warriorArmor - amount;
            }

            if (warriorHP <= 0) {
                displayForWarrior(0, 0);
                endGame(false);
            } else {
                displayForWarrior(warriorHP, warriorArmor);
            }
        } else if (target.equals("Priest")) {

            if (amount > priestArmor) {
                priestHP = priestHP - (amount - priestArmor);
                priestArmor = 0;
            } else {
                priestArmor = priestArmor - amount;
            }

            if (priestHP <= 0) {
                displayForPriest(0, 0);
                endGame(true);
            } else {
                displayForPriest(priestHP, priestArmor);
            }

        }
    }

    public void warriorGetArmor(View v) {
        // get +2 armor
        warriorArmor += 2;
        displayForWarrior(warriorHP, warriorArmor);
        Toast.makeText(this, "You gain 2 Armor!", Toast.LENGTH_SHORT).show();
        findViewById(R.id.warrior_add_armor).setEnabled(false);
    }

    public void priestHeal(View v) {
        // heal 2 hp unless it's already at max=30 hp
        if (priestHP < 29) {
            priestHP += 2;
            Toast.makeText(this, "You heal 2 HP!", Toast.LENGTH_SHORT).show();
        } else if (priestHP == 29) {
            priestHP += 1;
            Toast.makeText(this, "You heal 1 HP!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Health already at max!", Toast.LENGTH_SHORT).show();
        }

        displayForPriest(priestHP, priestArmor);
        findViewById(R.id.priest_heal).setEnabled(false);
    }

    public void priestSpell(View v) {

        int spell = (new Random()).nextInt(2);

        if (spell == 1) {
            // get +3 armor and deal 3 damage
            Toast.makeText(this, "You gain 3 Armor and deal 3 damage to Warrior!", Toast.LENGTH_SHORT).show();
            priestArmor += 3;
            displayForPriest(priestHP, priestArmor);
            dealDamage("Warrior", 3);

        } else if (spell == 2) {
            // deal 5 damage to both players
            Toast.makeText(this, "You deal 5 damage to both players!", Toast.LENGTH_SHORT).show();
            dealDamage("Warrior", 5);
            dealDamage("Priest", 5);
        } else {
            // remove opponent's armor
            Toast.makeText(this, "You remove Warrior's Armor!", Toast.LENGTH_SHORT).show();
            warriorArmor = 0;
            displayForWarrior(warriorHP, warriorArmor);

        }

        findViewById(R.id.priest_spell).setEnabled(false);
    }

    public void warriorSpell(View v) {

        int spell = (new Random()).nextInt(2);

        if (spell == 1) {
            // deal 2 damage to a random player
            if ((new Random()).nextBoolean()) {
                Toast.makeText(this, "You deal 2 damage to yourself!", Toast.LENGTH_SHORT).show();
                dealDamage("Warrior", 2);
            } else {
                Toast.makeText(this, "You deal 2 damage to Priest!", Toast.LENGTH_SHORT).show();
                dealDamage("Priest", 2);
            }

        } else if (spell == 2) {
            // get +5 armor
            Toast.makeText(this, "You gain 5 Armor!", Toast.LENGTH_SHORT).show();
            warriorArmor += 5;
            displayForWarrior(warriorHP, warriorArmor);
        } else {
            // deal 4 dmg to opponent
            Toast.makeText(this, "You deal 4 damage to Priest!", Toast.LENGTH_SHORT).show();
            dealDamage("Priest", 4);
        }

        findViewById(R.id.warrior_spell).setEnabled(false);
    }

    public void endTurn(View v) {
        nextTurn();
    }

    private void nextTurn() {

        warriorsTurn = !warriorsTurn;

        findViewById(R.id.priest_spell).setEnabled(!warriorsTurn);
        findViewById(R.id.priest_attack).setEnabled(!warriorsTurn);
        findViewById(R.id.priest_heal).setEnabled(!warriorsTurn);

        findViewById(R.id.warrior_spell).setEnabled(warriorsTurn);
        findViewById(R.id.warrior_attack).setEnabled(warriorsTurn);
        findViewById(R.id.warrior_add_armor).setEnabled(warriorsTurn);

    }

    private void endGame(boolean warriorWins) {
        if (warriorWins) {
            Toast.makeText(this, R.string.warrior_wins, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.priest_wins, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.priest_spell).setEnabled(false);
        findViewById(R.id.priest_attack).setEnabled(false);
        findViewById(R.id.priest_heal).setEnabled(false);

        findViewById(R.id.warrior_spell).setEnabled(false);
        findViewById(R.id.warrior_attack).setEnabled(false);
        findViewById(R.id.warrior_add_armor).setEnabled(false);

        findViewById(R.id.end_round).setEnabled(false);
    }


}
