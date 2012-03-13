import string, time, os, copy


# courseANDtimes HOLDS THE COURSES AND ALL THEIR INFO, CAN BE USED FOR TESTING.
courseANDtimes = []
#courseANDtimes = [('math',[ [1300,1400,'m'],[1500,1600,'mw'] ]),\
					#('sci',[ [1550,1900,'mwf'] ]),\
					#('hist', [ [900,1100,'mw'],[1200,1400,'tr'], [1500,1600,'mwf'] ]), \
					#('eng', [ [700,800,'f'],[1200,1300,'t'],[1400,1600,'mwf'] ])]

# NOTE:
#	NEED TO WORK ON DAYS <done>
#	NEED TO FIND A BETTER SORTING ALGORITH TO FIND ALL
#		POSSIBLE SCHEDULES.

# FINDS ALL POSSIBLE SCHEDULES
def combinate(x):
    makelist = []
    for n in x[0]:
        makelist.append(n)
    for n in range(1, len(x)):
        makelist = combine(makelist, x[n])
    print "PRINTING MAKELIST:"
    return makelist

def combine(x, combinant):
    numbertoremove = len(x)
    lol = copy.copy(x)
    for n in x:
        for m in combinant:
            lol.append(n + [m])
    lol = lol[numbertoremove:]
    print "PRINTING LIST-OF-LISTS:"
    print lol
    return lol

#################################
##def factor1(x):
##	all_list = []
##	for a in x[0]:
##
##		temp = [a]
##		all_list.append(temp)
##
##	return all_list
##
##def factor2(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			
##			temp = [a,b]
##			all_list.append(temp)
##
##	return all_list
##
##def factor3(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			for c in x[2]:
##				
##				temp = [a,b,c]
##				all_list.append(temp)
##
##	return all_list
##
##def factor4(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			for c in x[2]:
##				for d in x[3]:
##					temp = [a,b,c,d]
##					all_list.append(temp)
##
##	return all_list
##
##def factor5(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			for c in x[2]:
##				for d in x[3]:
##					for e in x[4]:
##					
##						temp = [a,b,c,d,e]
##						all_list.append(temp)
##
##	return all_list
##
##def factor6(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			for c in x[2]:
##				for d in x[3]:
##					for e in x[4]:
##						for h in x[5]:
##
##							temp = [a,b,c,d,e,h]
##							all_list.append(temp)
##
##	return all_list
##
##def factor7(x):
##	all_list = []
##	for a in x[0]:
##		for b in x[1]:
##			for c in x[2]:
##				for d in x[3]:
##					for e in x[4]:
##						for h in x[5]:
##							for i in x[6]:
##
##								temp = [a,b,c,d,e,h,i]
##								all_list.append(temp)
##
##	return all_list
####################################################
def finalize(schedule):
	#print schedule ,'schedule'
	converted = []

	for time_slot in schedule:
		#print time_slot, 'time_slot'
	
		new_slot = []
		
	#	for i in range(1):

		con = revert( time_slot[:2] )	
		#print con, 'con'

		new_slot.append(con)
		new_slot.append( time_slot[2] )
		new_slot.append( time_slot[3] )

		converted.append( new_slot )

	#print converted, 'converted'

	return converted
####################################################

def convert(s):
    """x, convert string x into a list of two hours"""
    # CONVERTS A STRING CONTAINING THE TIME SLOT INTO A
	# LIST ==> EXAMPLE: [1300, 1900]

    # SPLITS THE STRING INTO TWO TIMES
    time = s.split('-')
    t = []

    for i in time:
        # MAKES THE TIMES SLOT LOWER CASE
        i = i.lower()

        # CONVERTS TIME SLOT FROM 12 HOURS TO A 24 HOUR TIME SLOT
        if 'pm' in i:
            i = i.split('p')
            i = i[0]
            i = i.split(':')
            i = int(i[0] + i[1])
            t.append(i + 1200)

        else :
            i = i.split('a')
            i = i[0]
            i = i.split(':')
            t.append(int(i[0] + i[1]))
    return t   
#####################################################


def revert(bi_hour):
    #print bi_hour, 'bi_hour'
    """x, reverts a list of two numbers back into 12 hour times"""
	# REVERTS A LIST OF TWO HOURS BACK TO 12 HOUR TIMES
	# TIME IS FORMATTED PROPERLY TO BE READABLE.
    new_time = []

    for l in bi_hour:
        
        # CHECKS IF HOURLY IS AFTER 'NOON',
        # IF IT IS TURNS IT INTO CORRECT TIME
        if l >= 1300:
            l = l - 1200

            l = str(l)
            l = l[:-2] + ':' + l[-2:] + ' pm'
        
        else :
            l = str(l)
            l = l[:-2] + ':' + l[-2:] + ' am'

        new_time.append(l)
    new_time = new_time[0] + ' - ' + new_time[1]

    #print new_time, 'new_time'

	# new_time IS A STRING
    return new_time

######################################################
## lists = ({ [11,13,'mwf','math'],[14,15,'t','hist'] }, { [11,13,'mwf','math'],[14,15,'t','hist'] })

#### HERE IS WHERE YOU CAN ADD ADDITIONAL FEATURES
# MUST FIND THE TIME SLOTS THAT HAVE ANY NUMBER OF DAYS IN COMMON.

def course_found(lists, class_dict):
	list_of_found = []

	for schedule in lists:
		print
		print 'MAIN SCHEDULE EVERYTHING STARTS FROM HERE'
		print '		----', schedule, '----'
		print '$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$'

		common_slots = [] # common_slots= { [11,13,mwf],[14,15,mwf] }

		while schedule != []:

			slots = []; slot_num = []
			print 'Here I find the slot with largest day'

			greatest = [ schedule[0], 0 ]
			# FIND THE TIME SLOT WITH THE MOST DAYS
			for i in range( len( schedule ) ):
				
				if len (schedule) > 1:

					try:
						print
						print greatest[0], schedule[i+1]
						print

						if len( greatest[0][2] ) < len( schedule[i+1][2] ):


							greatest = [ schedule[i+1], i+1 ]

					except IndexError:
						pass

				else:
					print 'schedule length = 1  so  greatest = schedule'
					greatest = [ schedule[i], i ]
			
			time.sleep(.01) #FOR TESTING PURPOSES
			print '=============================================='
			print
			print 'greatest:  ', greatest
			print

			slots.append( greatest[0] )
			
			
			if len( schedule ) > 1:
			
				for slot in range( len( schedule ) ):

					# NOTE: IF USER SPECIFIES A DAY THAT OVERLAPS ANOTHER DAY IT MIGHT NOT BE
					#   SEEN..  <============== FIXED!! :)

					# CHECK IF TIME SLOT WITH GREATEST NUM OF DAYS HAS A DAY IN COMMON
					# WITH ANOTHER TIMESLOT, IF IT DOES THAN IT CAN BE COMPARED WITH 
					# OTHER TIME SLOTS THAT ARE ALIKE.

					if len( set(schedule[ greatest[1] ][2]) & set(schedule[slot][2]) ) != 0:

						if greatest[1] != slot:

							slots.append( schedule[slot] )
							slot_num.append(slot)

			time.sleep(.01) #FOR TESTING PURPOSES

			print slots, 'slots'
			print '++++++++++++++++++++++++++++++++++++++'
			print schedule, 'schedule'
			print '++++++++++++++++++++++++++++++++++++++'
			print

			for i in slots:
				time.sleep(.02)
				print i, 'this slot in slots will be deleted'
				print 'these must be equal ===> |', schedule[schedule.index(i)], '<>', i

				del( schedule[ schedule.index(i) ] )
			
			common_slots.append( slots )
			print 'While looping'
		print 'LOOP ENDED'
		print

		################# break ###############
		print 'PART 2 NOW STARTS!'
		print '##################'

		found = []; works = True

		for slot in common_slots:
			print slot, 'slot in common slots'

			slot.sort(); print ''
			print 'IS IT WORKING: ', works

			if works == False:
				
				break

			else:

				for i in range(len( slot )):
			
					try:
				
						if slot[i][1] >= slot[i+1][0]:
							works = False
							break   # <=== VERY IMPORTANT

					except IndexError:
						print 'current found: ', found
						print
						print 'current slot', slot
						found = found + slot

		print len(courseANDtimes), 'length of courseANDtimes'
		if len(found) == len (courseANDtimes):
			print 'THIS SCHEDULE WORKS: '
			print found, '             :)           '
			list_of_found.append( found )

	#print '+++++++++++++++++++++++++++++++++++'
	#print list_of_found
	#print '+++++++++++++++++++++++++++++++++++'
	return list_of_found



##############################################################

def print_to_screen( schedule ):
	
	print '################### HERE IS WHERE I PRINT ##################### \n'
	
	for i in schedule:
		
		print string.ljust( i[2] + ':', 30 ),
		print string.ljust( i[0], 40 ),

		for num in i[1]:
			print num.upper() + '',

		print '\n'
		

####################### MAIN #################################

# WILL ASK FOR:
#     NUMBER OF CLASSES,
#     COURSE NAME,
#     NUMBER OF TIME SLOTS FOR COURSE
#     TIME SLOT ITSELF, THEN REPEATS
def main():
    
	while True:

		course_name = raw_input('course name (string):      ') 

		if course_name == "":
			break

		list_of_times = []

		while True:

			slot = raw_input('input time slot (12:00AM-1:00PM): ')
			if slot == "":	break
			day = raw_input('input slot days (mwf):            ')
				
			time = convert(slot) + [day]
				
			list_of_times.append(time)
		print		    
		courseANDtimes.append((course_name, list_of_times))
    
    # THINKING OF TURNING THE COURSE NAMES AND TIMES INTO A CLASS,
    # SO FAR COURSE NAME AND TIMES ARE HELD IN A DICTIONARY!

	class_dict = dict(i for i in courseANDtimes)
	###########################################
	values = []
	for name, class_times in class_dict.iteritems():
		everything = []

		for i in class_times:
			everything.append(i+[name])

		values.append(everything)

	x = len(values)
	# FACT IS ALL AVAILABLE TIMES
	fact = combinate(values)
##	if x == 1:	fact = factor1(values)
##	elif x == 2:	fact = factor2(values)
##	elif x == 3:	fact = factor3(values)
##	elif x == 4:	fact = factor4(values)
##	elif x == 5:	fact = factor5(values)
##	elif x == 6:	fact = factor6(values)
##	elif x == 7:	fact = factor7(values)

	#######################################
	# WILL GIVE A LIST OF FOUND SCHEDULES
	found_list = course_found(fact, class_dict)
	print '//////////////////////////////////////////'
	print found_list, 'found_list'

	final_list = []

	for schedule in found_list:

		# schedule IS A LIST OF SUCCESSFUL HOURS

		final_list.append(finalize( schedule ))

	################ > OUTPUT < ##########################
	
	# THIS WILL print OUT THE FINAL OUTPUT!

	for schedule in final_list:
		
		if os.name == "posix":
		    # Unix/Linux/MacOS/BSD/etc
		    os.system('clear')
		elif os.name in ("nt", "dos", "ce"):
		    # DOS/Windows
		    os.system('CLS')
		else:
		    # Fallback for other operating systems.
		    print '\n' * 100

		print_to_screen( schedule )
		raw_input('press enter to see next schedule')
		
	#######################################
	        


if __name__ == '__main__':
	main()
