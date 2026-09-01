# Demo data for screenshots

`Order.java` — `status` field flagged (no explicit EnumType);
`shippingMethod` not flagged (explicit STRING).

## How to get the screenshot

1. `./gradlew runIde` from `enum-ordinal-persistence-companion`, open
   this `demo/` folder as the project.
2. Full Screen, open `Order.java` — a warning should appear on the
   `status` field but not on `shippingMethod`.
3. Screenshot with both fields visible, save into
   `enum-ordinal-persistence-companion/docs/screenshots/`. Close the
   sandbox.
