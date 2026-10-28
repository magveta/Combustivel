# Projeto 06 - Controle de Combustível

Aplicativo Android feito em Java para registrar abastecimentos e acompanhar alguns dados do veículo.

## O que o aplicativo faz

- Registra data, hodômetro, litros, tipo de combustível e preço por litro.
- Permite escolher gasolina, etanol ou diesel usando RadioGroup.
- Mostra o histórico de abastecimentos.
- Calcula consumo médio em km/L, custo por quilômetro e média de km por dia entre abastecimentos.
- Compara o preço do etanol com o da gasolina usando a regra aproximada dos 70%.
- Guarda os registros no próprio celular usando SharedPreferences.
- Confere se os campos estão preenchidos e se os valores fazem sentido.

## Como testar

1. Abra a pasta do projeto no Android Studio.
2. Espere o Gradle terminar a sincronização.
3. Rode o aplicativo em um emulador ou celular Android.
4. Cadastre pelo menos dois abastecimentos, com o hodômetro do segundo maior que o primeiro.
5. Abra o histórico para ver os cálculos.
6. Na parte de comparação, digite os preços do etanol e da gasolina.

## Como são feitos os cálculos

- **Consumo médio:** quilômetros percorridos desde o abastecimento anterior divididos pelos litros do abastecimento atual.
- **Custo por km:** valor total do abastecimento atual dividido pelos quilômetros percorridos no intervalo.
- **Média por dia:** quilômetros percorridos divididos pelos dias entre as duas datas. Se as datas forem iguais, o aplicativo considera um dia.
- **Etanol ou gasolina:** o etanol costuma compensar quando custa até 70% do preço da gasolina. É uma estimativa e pode variar de acordo com o veículo.

## Tecnologias

Java, XML, Android Studio, RadioGroup, SharedPreferences e JSON.
