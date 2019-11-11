import pika
import json

def consumer_callback(ch, method, properties, body):
    novos_livros = json.loads(body)
    print('Quantidade de livros:', len(novos_livros))
    for livro in novos_livros:
        print('Nome:', livro['nome'], 'Editora:', livro['editora'])


connection = pika.BlockingConnection(pika.ConnectionParameters())

nome_fila = 'filaJson'
channel = connection.channel()
channel.queue_declare(queue=nome_fila)
channel.basic_consume(on_message_callback=consumer_callback, queue=nome_fila)

channel.start_consuming()