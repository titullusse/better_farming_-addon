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
