import random
import math
import sys
from typing import List

input: List[List[int]] = [[1, 0, -1], [0, 1, -1], [0, 0, -1], [1, 1, -1]]
expected_value = [0, 1, 1, 0]
weight_list = {}
population = {}
BIAS = -1
global first_best
global second_best


def generate_random_weight(random_seed_input):
    random.seed(random_seed_input)
    low: int = -3
    high: int = 3
    random_weight = random.uniform(low, high)
    return round(random_weight, 2)


def get_key(population_dict: object, search_value: object) -> object:
    for index, value in population_dict.items():
        if value == search_value:
            return index


def generate_random_probability(random_seed_input, low, high):
    random.seed(random_seed_input)
    return random.randint(low, high)


def sigmoid(x):
    return 1 / (1 + math.exp(-x))


def weight_generation_list():
    temp_weight_list = []
    for i in range(9):
        temp_weight_list.append(generate_random_weight(random.uniform(1, 100)))
    return temp_weight_list


def initial_weight_generation(index_population):
    for i in range(1, index_population):
        print("Initial Population {}".format(i))
        weight_list[i] = weight_generation_list()
        population_generation(i)


def population_cost_generation(key_weight):
    yy1: List[float] = []
    yy2: List[float] = []
    y: List[float] = []
    temp_y: List[float] = []
    temp_array_list = weight_list[key_weight]
    yy1.append(sigmoid(
        input[0][0] * temp_array_list[0] + input[0][1] * temp_array_list[1] + input[0][2] * temp_array_list[2]))
    yy1.append(sigmoid(
        input[1][0] * temp_array_list[0] + input[1][1] * temp_array_list[1] + input[1][2] * temp_array_list[2]))
    yy1.append(sigmoid(
        input[2][0] * temp_array_list[0] + input[2][1] * temp_array_list[1] + input[2][2] * temp_array_list[2]))
    yy1.append(sigmoid(
        input[3][0] * temp_array_list[0] + input[3][1] * temp_array_list[1] + input[3][2] * temp_array_list[2]))

    yy2.append(sigmoid(
        input[0][0] * temp_array_list[3] + input[0][1] * temp_array_list[4] + input[0][2] * temp_array_list[5]))
    yy2.append(sigmoid(
        input[1][0] * temp_array_list[3] + input[1][1] * temp_array_list[4] + input[1][2] * temp_array_list[5]))
    yy2.append(sigmoid(
        input[2][0] * temp_array_list[3] + input[2][1] * temp_array_list[4] + input[2][2] * temp_array_list[5]))
    yy2.append(sigmoid(
        input[3][0] * temp_array_list[3] + input[3][1] * temp_array_list[4] + input[3][2] * temp_array_list[5]))

    yy = [[yy1[0], yy2[0], BIAS], [yy1[1], yy2[1], BIAS], [yy1[2], yy2[2], BIAS], [yy1[3], yy2[3], BIAS]]

    temp_y.append(
        round(yy[0][0] * temp_array_list[6] + yy[0][1] * temp_array_list[7] + yy[0][2] * temp_array_list[8], 2))
    temp_y.append(
        round(yy[1][0] * temp_array_list[6] + yy[1][1] * temp_array_list[7] + yy[1][2] * temp_array_list[8], 2))
    temp_y.append(
        round(yy[2][0] * temp_array_list[6] + yy[2][1] * temp_array_list[7] + yy[2][2] * temp_array_list[8], 2))
    temp_y.append(
        round(yy[3][0] * temp_array_list[6] + yy[3][1] * temp_array_list[7] + yy[3][2] * temp_array_list[8], 2))

    if temp_y[0] > 0:
        y.append(1.0)
    else:
        y.append(0.0)

    if temp_y[1] > 0:
        y.append(1.0)
    else:
        y.append(0.0)
    if temp_y[2] > 0:
        y.append(1.0)
    else:
        y.append(0.0)
    if temp_y[3] > 0:
        y.append(1.0)
    else:
        y.append(0.0)
    print(y)
    if y[0] == 0 and y[1] == 1 and y[2] == 1 and y[3] == 0:
        print('########################################## Y SET VALUES #####################################')
        print(y)
        print('######################################### Weights ###########################################')
        print('Weight {}'.format(temp_array_list))
        print('#############################################################################################')
        sys.exit(1)
    cost = round(math.pow(expected_value[0] - temp_y[0], 2) + math.pow(expected_value[1] - temp_y[1], 2) + math.pow(
        expected_value[2] - temp_y[2], 2) + math.pow(expected_value[3] - temp_y[3], 2), 2)

    return cost


def population_generation(index_population):
    population[index_population] = population_cost_generation(index_population)


def selection_population():
    temp_select_array = []
    for i in range(1, len(population)):
        temp_select_array.append(population.get(i))
    temp_select_array.sort()
    global first_best
    first_best = get_key(population, temp_select_array[0])
    global second_best
    second_best = get_key(population, temp_select_array[1])
    return first_best


def cross_over_population():
    first_best_array_list = weight_list.get(first_best)
    second_best_array_list = weight_list.get(second_best)
    random_selection = generate_random_probability(random.uniform(1, 100), 1, 2)
    random_probability = generate_random_probability(random.uniform(1, 100), 0, 9)
    for i in range(random_probability):
        temp = first_best_array_list[i]
        first_best_array_list[i] = second_best_array_list[i]
        second_best_array_list[i] = temp
    if random_probability == 0:
        mutation_population(first_best_array_list)
    elif random_selection == 1:
        mutation_population(first_best_array_list)
    elif random_selection == 2:
        mutation_population(second_best_array_list)


def mutation_population(temp_new_off_spring):
    random_set_limit = generate_random_probability(random.uniform(1, 100), 0, 8)
    random_temp_limit = 8 - random_set_limit
    random_set_start = generate_random_probability(random.uniform(1, 100), 0, random_temp_limit)
    random_set_end = random_set_start + random_set_limit
    for i in range(random_set_start, random_set_end):
        temp_new_off_spring[i] = temp_new_off_spring[i] * -1
    weight_list[len(weight_list) + 1] = temp_new_off_spring
    population_generation(len(weight_list))


def genetic_algorithm():
    global weight_number
    i = 1
    while i < 3500000:
        answer_key = selection_population()
        temp_check_double = round(population[answer_key], 2)
        if temp_check_double < 0.2:
            print('Breaking')
            weight_number = answer_key
            print("Weight {}".format(weight_list.get(weight_number)))
            break
        cross_over_population()
        print("Genetic Algorithm {}".format(i))
        i += 1
        weight_number = answer_key
    return weight_number


class GeneticAlgorithm(object):
    initial_weight_generation(50)
    selection_population()
    cross_over_population()
    genetic_algorithm()
