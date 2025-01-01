# LR6_Async_Khortov

## Завдання
<p><b>Варіант-2</b></p>
<ul>
 <p>Напишіть програму, в якій будуть реалізовані наступні задачі:</p>
<li>кожні 10 секунд будуть оновлюватись певні дані у конкретному файлі
(наприклад, буде збільшуватись лічильник на 5 і це значення буде
дописуватись у файл);</li>

<li>створіть клас для запуску методу, який через 5 секунд після запуску
програми буде виводити повідомлення (наприклад, «5 секунд від
запуску програми»).</li>
</ul>

## Загальний опис рішення
<p><b>Проект складається з частин:</b></p>
<ul>
  <li>1. Вивід лічильника в консоль</li>
  <li>2. Вивід лічильника на сторінку</li>
  <li>3. Вивід повідомлення про роботу в консоль</li>
  <li>4. Вивід повідомлення про роботу на сторінку</li>
</ul>

## Запуск Spring 
<p>Після ініціалізації проекту на ресурсі https://start.spring.io/ було додано частину запуску програми. Спершу ініціалізуються змінні та знаходиться шлях до текстовий файл. Метод run() перезаписується із введенням алгоритму створення файлу у разі відсутності. Також викликається функція RunInfo.printInfo() для виведення повідомлення через 5 секунд</p>
<p>Запуск та ініціалізація мають такий вигляд:</p>

    	private static final Path FILE_PATH = Path.of("counter.txt");
    	private int counter = 0;
    	private String runtimeMessage = " ";
    
    	public static void main(String[] args) {
    		SpringApplication.run(Application.class, args);
    	}
    
    	@Override
    	public void run(String... args) throws Exception {
    
    		if (!Files.exists(FILE_PATH)) {
    			Files.createFile(FILE_PATH);
    		}
    
    		runtimeMessage = RunInfo.printInfo();
    
    	}

        
       
        
 
## Клас RunInfo
Клас має такий вигляд:


     public class RunInfo {
         private static String runtimeMessage = "";
     
         public static String printInfo() {
             ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
     
     
             Future<String> future = executor.schedule(() -> {
                 runtimeMessage = "5 seconds passed since the launch!";
                 System.out.println(runtimeMessage);
                 return runtimeMessage;
             }, 5, TimeUnit.SECONDS);
     
             try {
                 return future.get();
             } catch (Exception e) {
                 throw new RuntimeException("Error waiting for task completion", e);
             } finally {
                 executor.shutdown();
             }
         }
     
         public static String getRuntimeMessage() {
             return runtimeMessage;
         }
     }


Функція printInfo() модифікує змінну runtimeMessage із затримкою у 5 секунд. Потім метод виводить повідоилення в консоль та блокується за участі  get() для повернення змінної runtime


            
## Запис у текстовий файл
За допомогою @Scheduled(fixedRate = 10000) можна вводити період функції. В її тілі зміна counter збільшується на 5, а потім записуються у новий рядок файлу. В консоль виводиться результат процесу.
<p>Функція має такий вигляд:</p>

  	@Scheduled(fixedRate = 10000)
  	public void updateFile() {
  		counter += 5;
  		String content = "Counter value: " + counter + "\n";
  		try {
  			Files.writeString(FILE_PATH, content, StandardOpenOption.APPEND);
  			System.out.println("Updated file with: " + content.trim());
  		} catch (IOException e) {
  			System.err.println("Failed to update file: " + e.getMessage());
  		}
  	}
    


   

## Оновлення сторінки
Надсилання данних відбувається через реакцію на ajax-запити:


  	class CounterController {
  		@GetMapping("/counter")
  		public String getCounterValue() {
  			return String.valueOf(counter);
  		}
  
  		@GetMapping("/runtime-message")
  		public String getRuntimeMessage() {return runtimeMessage;}
  	}

Була створена сторінка у resources/static/ Сторінка має такий алгортим запитів та модифікації вмісту:

    <script>
        function updateCounter() {
            fetch('/counter')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('counter').innerText = "Current counter value: " + data;
                });
        }

        function updateRuntimeMessage() {
            fetch('/runtime-message')
                .then(response => response.text())
                .then(data => {
                    if (data) {
                        document.getElementById('logTime').innerText = data;
                    }
                });
        }

        setInterval(updateCounter, 1000);
        setInterval(updateRuntimeMessage, 1000);

        window.onload = () => {
            updateCounter();
            updateRuntimeMessage();
        };
    </script>
