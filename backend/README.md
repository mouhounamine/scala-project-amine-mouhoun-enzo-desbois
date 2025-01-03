# Workflow Git pour Intégration Continue et Livraison Continue

## Branche de développement

Le workflow Git du projet repose sur l’utilisation de branches de développement distinctes pour chaque développeur, et d’une branche `master` qui est toujours en phase avec la production.

### Création de branches

- **Branche principale (`master`)** : Cette branche doit toujours être identique à la version en production. Toutes les modifications validées doivent y être fusionnées.
- **Branche de développement (`develop/NOM_DU_DEVELOPPEUR`)** : Chaque développeur crée une branche à partir de `master`, avec son nom pour identifier ses contributions. Exemple : `develop/amine`.

### Étapes à suivre

1. **Création d’une branche de développement** :

   - Il est important de partir de `master` pour toute nouvelle fonctionnalité ou tâche.
   - Pour créer une branche de développement à partir de `master` :
     ```bash
     git checkout master
     git pull origin master
     git checkout -b develop/NOM_DU_DEVELOPPEUR
     ```
   - Exemple pour "Amine" :
     ```bash
     git checkout -b develop/amine
     ```

2. **Travail sur la branche** :

   - Ajoutez, modifiez ou supprimez des fichiers selon les besoins du projet.
   - Une fois vos modifications effectuées, validez-les avec un commit :
     ```bash
     git add .
     git commit -m "Description des modifications"
     ```

3. **Poussée vers le dépôt distant** :
   - Une fois le commit effectué, poussez la branche de développement sur le dépôt distant :
     ```bash
     git push origin develop/NOM_DU_DEVELOPPEUR
     ```

### Merge Request (MR)

4. **Création d’une Merge Request** :

   - Une fois satisfait des changements sur votre branche de développement, ouvrez une Merge Request vers `master`.
   - Indiquez clairement les modifications apportées dans la description de la MR.

5. **Revue et validation** :

   - L'autre développeur devra examiner la Merge Request. Si des ajustements sont nécessaires, des commentaires seront ajoutés.
   - Une fois validée, la MR pourra être fusionnée avec `master`.

6. **Fusion de la branche dans `master`** :

   - Une fois la MR validée, il est temps de fusionner votre branche dans `master`.

7. **Suppression de la branche de développement** :
   - Une fois la fusion effectuée, il est recommandé de supprimer la branche de développement :
     ```bash
     git branch -d develop/NOM_DU_DEVELOPPEUR
     git push origin --delete develop/NOM_DU_DEVELOPPEUR
     ```

### Intégration Continue et Livraison Continue (CI/CD)

- **Intégration Continue (CI)** : Dès qu’une branche est poussée, une série de tests automatiques (unitaires, linting, etc.) sont lancés pour vérifier la qualité du code.
- **Livraison Continue (CD)** : Après chaque fusion de branche dans `master`, une pipeline CI/CD déploie automatiquement les modifications vers l’environnement de production, si tout est validé par les tests.

---

### Résumé du Flux de Travail

1. Partir de `master` pour créer une branche `develop/NOM_DU_DEVELOPPEUR`.
2. Travailler sur la branche, faire des commits, puis pousser sur le dépôt distant.
3. Ouvrir une Merge Request vers `master` pour que l’autre développeur puisse vérifier.
4. Fusionner la MR après validation.
5. Supprimer la branche de développement après fusion.
6. Le processus CI/CD prend en charge l’intégration et le déploiement automatique.

---

### Conclusion

Ce flux de travail permet de gérer efficacement les contributions tout en maintenant `master` dans un état prêt pour la production. L’intégration et la livraison continues garantissent que les modifications sont testées et déployées sans risque.
