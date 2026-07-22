# Better Farming ++ : Configurable Crate Radius (Addon)

Addon **NeoForge 1.21.1** pour le mod
[Better Farming ++](https://modrinth.com/mod/better-farming-plus)
([code source](https://github.com/MeherBenSalem/better_farming_plus)) qui permet de
**modifier le rayon d'action du Farming Crate**.

Dans le mod de base, le Farming Crate récolte les cultures matures dans une zone
dont le rayon est codé en dur (équivalent à un rayon de 3, soit une zone 7x7).
Cet addon rend ce rayon configurable.

## Fonctionnement

L'addon utilise un **Mixin** qui s'injecte dans la procédure
`FarmingCrateOnTickUpdateProcedure` du mod de base et remplace la boucle de
récolte à rayon fixe par une boucle dont le rayon est lu depuis un fichier de
configuration. La logique de récolte (drop des cultures, remise à zéro, collecte
des objets dans le coffre) est reproduite à l'identique, seul le rayon change.

La zone de collecte des objets est centrée sur le coffre et couvre un bloc de
plus que le rayon de récolte, afin que les objets récoltés en bordure de zone
soient bien ramassés.

Ce module ne s'exécute que **côté serveur** : la récolte, les drops et le
remplissage du coffre sont calculés sur le serveur (ou le serveur intégré en
solo), puis synchronisés normalement vers les clients.

## Écran de configuration en jeu

L'addon enregistre l'écran de configuration intégré de NeoForge. Avec un mod
de type **Mod Menu** pour NeoForge (par exemple
[Better ModList](https://modrinth.com/mod/better-modlist) ou
[Neo Mod Menu](https://github.com/1foxy2/neo_mod_menu)) installé, un bouton
**config** apparaît à côté de ce mod dans la liste des mods, permettant de
modifier `harvest_radius` directement en jeu sans éditer le fichier `.toml`.

## Configuration

Fichier généré : `config/better_farming_radius-common.toml`

```toml
[farming_crate]
	# Action radius (in blocks) of the Farming Crate.
	# The crate scans a (2 * radius + 1) x (2 * radius + 1) square centered on itself.
	# Default is 3 (a 7x7 area). Increase for a wider harvest area.
	# Range: 0 ~ 32
	harvest_radius = 3
```

- `harvest_radius = 3` → zone 7x7 (comportement par défaut du mod de base)
- `harvest_radius = 5` → zone 11x11
- etc.

## Prérequis

- Minecraft **1.21.1**
- **NeoForge** 21.1+
- Le mod **Better Farming ++** (`better_farming_plus`) doit être installé.

## Compilation

```bash
./gradlew build
```

Le jar est généré dans `build/libs/`.
